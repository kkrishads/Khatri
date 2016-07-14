<?php
include 'config.php';
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
// create json response
$responses = array();

// read JSon input
$data_back = json_decode(file_get_contents('php://input'));
 if($data_back!=null && $data_back!="")
 {

// set json string to php variables

$deviceType = str_replace("'","",$data_back->{"deviceType"});
if($deviceType!=null && $deviceType=="Android")
{
	
	// create conferences array 
	 $responses["Contacts"] = array();
	
		$sql_contact="SELECT * FROM `contact_list`";
	
	$result_contact=mysql_query($sql_contact) or die(mysql_error());
	while($row_contact=mysql_fetch_assoc($result_contact))
	{
			 $contact = array();
			 $contact=$row_contact;
	     array_push($responses["Contacts"], $contact);
	}
	$status="S";
	$responses["Users"] = array();
	$users = array("status" => $status);
	array_push($responses["Users"], $users);
	
} else {
	 $status="Invalid Access";
	$responses["Users"] = array();
	$users = array("status" => $status);
	array_push($responses["Users"], $users);
}
 } else {
	  $status="Invalid Access";
	$responses["Users"] = array();
	$users = array("status" => $status);
	array_push($responses["Users"], $users);
 } 
} else {
	 $status="Invalid Access";
	$responses["Users"] = array();
	$users = array("status" => $status);
	array_push($responses["Users"], $users);
} 

// set header as json
header("Content-type: application/json");
 
// send response
echo json_encode($responses);
mysql_close();



?>