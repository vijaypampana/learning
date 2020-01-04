package learning.exceptions

class TestException {

    static void main(String[] args) {
        try {
            throw new DBException.NoData("No Data retrieved from DB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
