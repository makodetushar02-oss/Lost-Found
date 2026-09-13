public class claim {
    int claimId;
    String proof;
    int claimedBy;
    String itemName;
    String Location ;
    String Status;

    public claim(int claimId, String proof, int claimedBy, String itemName, String location, String status) {
        this.claimId = claimId;
        this.proof = proof;
        this.claimedBy = claimedBy;
        this.itemName = itemName;
        Location = location;
        Status = status;
    }
    public claim( String proof, int claimedBy, String itemName, String location, String status) {
        this.proof = proof;
        this.claimedBy = claimedBy;
        this.itemName = itemName;
        Location = location;
        Status = status;
    }

    public int getClaimId() {
        return claimId;
    }

    public String getProof() {
        return proof;
    }

    public int getClaimedBy() {
        return claimedBy;
    }

    public String getItemName() {
        return itemName;
    }

    public String getLocation() {
        return Location;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
