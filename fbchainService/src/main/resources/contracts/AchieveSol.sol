pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract AchieveSol {

    TableFactory tableFactory;
    string constant TABLE_NAME = "achieve";
    
    constructor() public {
        tableFactory = TableFactory(0x1001);
        tableFactory.createTable(TABLE_NAME, "edu_id", "title,acquire_time,certify_file");
    }
    
    //insert records
    function insert(string eduId, string[] titleList, string[] acquireTimeList, string[] certifyFileList)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_NAME);
        int256 count = 0;

        // must keep the three lists in the same length.
        for(uint256 i = 0; i < titleList.length; i++){
            Entry entry = table.newEntry();
            entry.set("edu_id", eduId);
            entry.set("title", titleList[i]);
            entry.set("acquire_time", acquireTimeList[i]);
            entry.set("certify_file", certifyFileList[i]);
            
            count = count + table.insert(eduId, entry);
        }
       
        return count;
    }
    
     //select records
    function select(string eduId)
    public
    view
    returns (string[] titleList, string[] acquireTimeList, string[] certifyFileList)
    {
        Table table = tableFactory.openTable(TABLE_NAME);
        Entries entries = table.select(eduId, table.newCondition());
        
        titleList = new string[](uint256(entries.size()));
        acquireTimeList = new string[](uint256(entries.size()));
        certifyFileList = new string[](uint256(entries.size()));

        for (int256 i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);

            titleList[uint256(i)] = entry.getString("title");
            acquireTimeList[uint256(i)] = entry.getString("acquire_time");
            certifyFileList[uint256(i)] = entry.getString("certify_file");
        }
    }
}