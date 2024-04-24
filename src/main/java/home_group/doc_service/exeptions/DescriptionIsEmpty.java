package home_group.doc_service.exeptions;

public class DescriptionIsEmpty extends RuntimeException {
    public DescriptionIsEmpty(String message) {
        super(message);

    }
}
