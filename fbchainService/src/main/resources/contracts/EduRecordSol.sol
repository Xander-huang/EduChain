pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Table.sol";

contract EduRecordSol {
    
    TableFactory tableFactory;
    string constant TABLE_NAME = "edu_record";
    
    constructor() public {
        tableFactory = TableFactory(0x1001);
        tableFactory.createTable(TABLE_NAME, "edu_id", "person_id,edu_type,node_id,node_name,begin_time,end_time,major_name,acquire_credit,graduate_credit,certify_file");
    }
    
     //insert record
    function insert(string eduId, string personId, string eduType, int256 nodeId, string nodeName, string beginTime, string endTime, string majorName, int256 acquireCredit, int256 graduateCredit, string certifyFile)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_NAME);
        Entry entry = table.newEntry();
        
        entry.set("edu_id", eduId);
        entry.set("person_id", personId);
        entry.set("edu_type", eduType);
        entry.set("node_id", nodeId);
        entry.set("node_name", nodeName);
        entry.set("begin_time", beginTime);
        entry.set("end_time", endTime);
        entry.set("major_name", majorName);
        entry.set("acquire_credit", acquireCredit);
        entry.set("graduate_credit", graduateCredit);
        entry.set("certify_file", certifyFile);

        return table.insert(eduId, entry);
    }
    
     //select record
    function select(string eduId)
    public
    view
    returns (string personId, string eduType, int256 nodeId, string nodeName, string beginTime, string endTime, string majorName, int256 acquireCredit, int256 graduateCredit, string certifyFile)
    {
        Table table = tableFactory.openTable(TABLE_NAME);
        Entries entries = table.select(eduId, table.newCondition());
        Entry entry = entries.get(0);
        
        personId = entry.getString("person_id");
        eduType = entry.getString("edu_type");
        nodeId = entry.getInt("node_id");
        nodeName = entry.getString("node_name");
        beginTime = entry.getString("begin_time");
        endTime = entry.getString("end_time");
        majorName = entry.getString("major_name");
        acquireCredit = entry.getInt("acquire_credit");
        graduateCredit = entry.getInt("graduate_credit");
        certifyFile = entry.getString("certify_file");
    }
}