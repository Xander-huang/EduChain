pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "./Table.sol";

contract PersonSol {
    
    TableFactory tableFactory;
    string constant TABLE_NAME = "person";
    
    constructor() public {
        tableFactory = TableFactory(0x1001); 
        tableFactory.createTable(TABLE_NAME, "id", "name,gender,nation,birthday");
    }

    function select(string id)
    public
    view
    returns (string name, string gender, string nation, string birthday)
    {
        Table table = tableFactory.openTable(TABLE_NAME);
        Entries entries = table.select(id, table.newCondition());
        
        Entry entry = entries.get(0);
        name =entry.getString("name");
        gender = entry.getString("gender");
        nation = entry.getString("nation");
        birthday = entry.getString("birthday");
    }
    
    function insert(string id, string name, string gender, string nation, string birthday)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_NAME);
        Entry entry = table.newEntry();
        
        entry.set("id", id);
        entry.set("name", name);
        entry.set("gender", gender);
        entry.set("nation", nation);
        entry.set("birthday", birthday);

        return table.insert(id, entry);
    }
 
}