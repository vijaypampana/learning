package learning.exceptions

class DBException {

    //SQL Exception
    static class BADExecution extends BaseException {
        BADExecution(String msg) {
            super(msg);
        }
    }

    //No Data Exception
    static class NoData extends BaseException {
        NoData(String msg) {
            super(msg);
        }
    }
}
