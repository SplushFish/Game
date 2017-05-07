package net.dragonclaw.sqlite;

import java.util.ArrayList;
import java.util.List;

public class SQLTable<T extends SQLDataType> {

    private List<T> table = new ArrayList<T>();

    public SQLTable() {

    }

}
