public class Contract {

    private int contractId;
    private String contractName;

    public Contract(
            int contractId,
            String contractName) {

        this.contractId = contractId;
        this.contractName = contractName;
    }

    public int getContractId() {
        return contractId;
    }

    public String getContractName() {
        return contractName;
    }

    @Override
    public String toString() {

        return contractId
                + " - "
                + contractName;
    }
}