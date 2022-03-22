create or replace TRIGGER "AUTORITES"."NOTICE_INDEXING_TRIGGER" after INSERT or UPDATE or DELETE
ON NOTICESBIBIO_TEST
FOR EACH ROW
--------------------------------------------------------------------------------
DECLARE
    notice_id      number;
    notice_content xmltype;
    action         varchar2 (128);
--------------------------------------------------------------------------------
BEGIN

    IF INSERTING THEN
        action := 'insert';
        notice_id := :new.id;
        notice_content := :new.data_xml;
    END IF;

    IF UPDATING THEN
        action := 'update';
        notice_id := :new.id;
        notice_content := :new.data_xml;
    END IF;

    IF DELETING THEN
        action := 'delete';
        notice_id := :old.id;
        notice_content := :old.data_xml;
    END IF;

    IF notice_content IS NOT NULL THEN
        DBMS_OUTPUT.PUT_LINE ( '[INFO][NOTICESBIBIO][TRIGGER] ' || action || ' notice with id ' || notice_id );
        CALL_NOTICE_INDEXING_SERVICE(action,notice_id,notice_content);
    END IF;

END;