package net.dragonclaw.sqlite;

public class SQLResult {

    private Object data = null;
    private final String resultMessage;
    private boolean succes = true;
    private boolean error = false;

    public SQLResult(SQLDataType type) {
        data = type;
        resultMessage = "succes";
    }

    public SQLResult(SQLTable<?> table) {
        data = table;
        resultMessage = "succes";
    }

    public SQLResult(String failureReason) {
        resultMessage = failureReason;
        succes = false;
        error = true;
    }

    public SQLResult(String result, boolean succes) {
        resultMessage = result;
        this.succes = succes;
    }

    public boolean isSucces() {
        return succes;
    }
    
    public boolean isError(){
        return error;
    }

    public boolean isSuccesTable() {
        return data instanceof SQLTable<?> && succes;
    }

    public boolean isSuccesSingleton() {
        return data instanceof SQLDataType && succes;
    }

    public SQLDataType asDataType() {
        try {

            return (SQLDataType) data;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public SQLTable<?> asTable() {
        try {
            return (SQLTable<?>) data;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public <T extends SQLDataType> SQLTable<T> asTable(Class<T> refrence) {
        try {
            return (SQLTable<T>) data;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public String getResultMessage() {
        return resultMessage;
    }

}
