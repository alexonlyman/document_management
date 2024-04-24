package home_group.doc_service.exeptions;

public class EmailAllreadyExist extends RuntimeException {
    public EmailAllreadyExist(String message) {
        super(message);

    }
}
