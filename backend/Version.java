public class Version {

    private int versionNumber;
    private String fileName;

    public Version(
            int versionNumber,
            String fileName) {

        this.versionNumber = versionNumber;
        this.fileName = fileName;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {

        return "Version "
                + versionNumber
                + " - "
                + fileName;
    }
}