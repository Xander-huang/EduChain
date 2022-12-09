pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract CourseSol {

    TableFactory tableFactory;
    string constant TABLE_NAME = "course";
    
    constructor() public {
        tableFactory = TableFactory(0x1001);
        tableFactory.createTable(TABLE_NAME, "edu_id", "name,credit,certify_file");
    }
    
    //insert records
    function insert(string eduId, string[] nameList, string[] creditList, string[] certifyFileList)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_NAME);
        int256 count = 0;

        // must keep the three lists in the same length.
        for(uint256 i = 0; i < nameList.length; i++){
            Entry entry = table.newEntry();
            entry.set("edu_id", eduId);
            entry.set("name", nameList[i]);
            entry.set("credit", creditList[i]);
            entry.set("certify_file", certifyFileList[i]);

            count = count + table.insert(eduId, entry);
        }

        return count;
    }

     //select records
    function select(string eduId)
    public
    view
    returns (string[] nameList, string[] creditList, string[] certifyFileList)
    {
        Table table = tableFactory.openTable(TABLE_NAME);
        Entries entries = table.select(eduId, table.newCondition());

        nameList = new string[](uint256(entries.size()));
        creditList = new string[](uint256(entries.size()));
        certifyFileList = new string[](uint256(entries.size()));

        for (int256 i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);

            nameList[uint256(i)] = entry.getString("name");
            creditList[uint256(i)] = entry.getString("credit");
            certifyFileList[uint256(i)] = entry.getString("certify_file");
        }
    }
}