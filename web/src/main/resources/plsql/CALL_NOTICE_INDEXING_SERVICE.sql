create or replace PROCEDURE CALL_NOTICE_INDEXING_SERVICE(action in varchar2, notice_id in number, notice_content in xmltype)
AS
--------------------------------------------------------------------------------
    -- Requete et Reponse HTTP
    http_request             UTL_HTTP.req;
    http_response            UTL_HTTP.resp;
    -- Serveur du service d'indexation
    request_host_name        VARCHAR2 (128)   := '';
    request_port             VARCHAR2 (128)   := '80';
    request_method           VARCHAR2 (128)   := 'PUT';
    request_full_url         varchar2 (2000)   := 'http://';
    request_api_url          VARCHAR2 (128)   := '/periscope/api/v1/notices';
    -- Contenu de la requete HTTP
    request_content          CLOB;
    request_content_length   binary_integer;
    request_content_type     VARCHAR2 (128)   := 'application/xml;charset=UTF-8';
    -- Contenu de la reponse HTTP
    response_content          CLOB             := null;
    -- Buffer
    buffer_size               NUMBER (10)      := 512;
    buffer_raw_data           RAW (512);
    buffer_content            CLOB             := null;
    amount                    pls_integer      := 4000;
    offset                    pls_integer      := 1;
    -- Exception
    api_exception             EXCEPTION;
    PRAGMA EXCEPTION_INIT(api_exception, -20501 );
    -- Debug
    debug_mode                BOOLEAN          := true;
    header_key                VARCHAR2 (256);
    header_value              VARCHAR2 (1024);
--------------------------------------------------------------------------------
BEGIN
    -- On recupere le document XML
    select XMLSERIALIZE (CONTENT notice_content as CLOB) into request_content from dual;

    IF action = 'insert' THEN
        request_method := 'PUT';
    ELSIF action = 'update' THEN
        request_method := 'POST';
    ELSIF action = 'delete' THEN
        request_method := 'DELETE';
    ELSE
        raise_application_error(-20501,'Unable to decode action "' || action || '". Require "insert" or "update" or "delete"');
    END IF;

    -- On construit l'URL de la requete
    request_full_url := request_full_url || request_host_name || ':' || request_port || request_api_url;

    IF debug_mode THEN
        -- On affiche la requete HTTP
        dbms_output.put_line('[DEBUG][NOTICESBIBIO][INDEXING] ' || request_method || ' ' || request_full_url);
        dbms_output.put_line('[DEBUG][NOTICESBIBIO][INDEXING] HTTP request Content-Type: ' || request_content_type);
        dbms_output.put_line('[DEBUG][NOTICESBIBIO][INDEXING] HTTP request Content-Lenght: ' || length (request_content));
        dbms_output.put_line('[DEBUG][NOTICESBIBIO][INDEXING] HTTP request Body: ' || request_content);
    END IF;

    -- On construit la requete HTTP
    http_request := UTL_HTTP.begin_request(
        url               => request_full_url,
        method            => request_method,
        http_version      => 'HTTP/1.1'
    );

    UTL_HTTP.set_header(http_request, 'User-Agent', 'Mozilla/4.0');
    -- ATTENTION --
    -- UTL_HTTP remplit le Host dans l'entete HTTP automatiquement avec le hostname de l'URL
    -- Il ne faut pas rajouter à la main le Host dans l'entete HTTP sous peine de dupliquer
    -- la clé Host et produire une erreur 400 sur la plupart des serveurs (tomcat inclut)
    --UTL_HTTP.set_header(http_request, 'Host', request_host_name || ':' || request_port); NE PAS UTULISER CETTE LIGNE
    UTL_HTTP.set_header(http_request, 'Connection', 'close');
    UTL_HTTP.set_header(http_request, 'Content-Type', request_content_type);
    UTL_HTTP.set_header(http_request, 'Content-Length', length (request_content));
    UTL_HTTP.set_header(http_request, 'Transfer-Encoding', 'chunked' );
    UTL_HTTP.set_body_charset('utf8');

    request_content_length := DBMS_LOB.getlength(request_content);

    WHILE (offset < request_content_length)
    LOOP
       DBMS_LOB.read(request_content,
                     amount,
                     offset,
                     buffer_content);
        UTL_HTTP.write_text(http_request, buffer_content);
        offset := offset + amount;
    END LOOP;

    -- On envoit la requete HTTP et on recupere la reponse HTTP
    http_response := UTL_HTTP.get_response(http_request);

    IF debug_mode THEN
        -- On affiche la reponse HTTP
        dbms_output.put_line('[DEBUG][NOTICESBIBIO][INDEXING] ---------------------');
        dbms_output.put_line('[DEBUG][NOTICESBIBIO][INDEXING] HTTP response code:' || http_response.status_code);
        dbms_output.put_line('[DEBUG][NOTICESBIBIO][INDEXING] HTTP response header: ');
        FOR i IN 1..UTL_HTTP.get_header_count(http_response) LOOP
            UTL_HTTP.get_header(http_response, i, header_key, header_value);
            dbms_output.put_line('[DEBUG][NOTICESBIBIO][INDEXING] ' || header_key || ': ' || header_value);
        END LOOP;
        dbms_output.put_line('[DEBUG][NOTICESBIBIO][INDEXING] ---------------------');
    END IF;

    BEGIN
         <<response_loop>>
         LOOP
            UTL_HTTP.read_raw(http_response, buffer_raw_data, buffer_size);
            response_content := response_content || UTL_RAW.cast_to_varchar2(buffer_raw_data);
         END LOOP response_loop;
        EXCEPTION
            -- Si erreur, on ferme la conexion
            WHEN UTL_HTTP.end_of_body
            THEN
                UTL_HTTP.end_response(http_response);
    END;

    -- On ferme les connexions
    IF http_request.private_hndl IS NOT NULL
    THEN
        UTL_HTTP.end_request(http_request);
    END IF;

    IF http_response.private_hndl IS NOT NULL
    THEN
        UTL_HTTP.end_response(http_response);
    END IF;

    IF debug_mode THEN
        -- On affiche le contenu de la reponse HTTP
        dbms_output.put_line('[DEBUG][NOTICESBIBIO][INDEXING] HTTP response content:' || response_content);
    END IF;

    -- On verifie le code de la reponse HTTP
    IF http_response.status_code = UTL_HTTP.HTTP_OK THEN
        DBMS_OUTPUT.PUT_LINE('[OK][NOTICESBIBIO][INDEXING] ' || action || ' with notice id ' || notice_id);
    ELSE
        raise_application_error(-20501,'HTTP return code ' || http_response.status_code || ' - HTTP content :' || response_content);
    END IF;

    EXCEPTION
        WHEN UTL_HTTP.request_failed THEN
            UTL_HTTP.end_response(http_response);
            raise_application_error(-20501,'Request HTTP failed with error ' || sqlerrm);
        WHEN others THEN
            raise_application_error(-20501,'Call indexing service failed with error ' ||  sqlerrm);
END;