<form action="" enctype="multipart/form-data" method="post">

<input id="fileToUpload" name="file" type="file" />

<input name="submit" type="submit" value="Upload" />

</form>

<?php
include 'config.php';
include 'Classes/PHPExcel/IOFactory.php';
if(isset($_POST['submit'])){
	
	if ( isset($_FILES["file"])) {
//if there was an error uploading the file
if ($_FILES["file"]["error"] > 0) {
echo "Return Code: " . $_FILES["file"]["error"] . "<br />";
}
else {


$inputfile=$_FILES["file"]["tmp_name"];
try {
	$objPHPExcel = PHPExcel_IOFactory::load($inputfile);
} catch(Exception $e) {
	die('Error loading file "'.pathinfo($inputfile,PATHINFO_BASENAME).'": '.$e->getMessage());
}

$allDataInSheet = $objPHPExcel->getActiveSheet()->toArray(null,true,true,true);
$arrayCount = count($allDataInSheet);  // Here get total count of row in that Excel sheet


for($i=2;$i<=$arrayCount;$i++){
$fname = trim($allDataInSheet[$i]["A"]);
$lname = trim($allDataInSheet[$i]["B"]);
$email = trim($allDataInSheet[$i]["C"]);
$mobile = trim($allDataInSheet[$i]["D"]);
$phone = trim($allDataInSheet[$i]["E"]);
$city = trim($allDataInSheet[$i]["F"]);
$area = trim($allDataInSheet[$i]["G"]);
$pincode = trim($allDataInSheet[$i]["H"]);
$address = trim($allDataInSheet[$i]["I"]);
$latitude = trim($allDataInSheet[$i]["J"]);
$longitude = trim($allDataInSheet[$i]["K"]);

$query = "SELECT fname FROM contact_list WHERE mobile = '".$mobile."' and email = '".$email."' and phone='".$phone."'";
$sql = mysql_query($query);
$recResult = mysql_fetch_array($sql);
$existName = $recResult["fname"];
if($existName=="") {
$insertTable= mysql_query("insert into contact_list (fname, lname,email,mobile,phone,city,area,pincode,address,latitude,longitude)
 values('".$fname."', '".$lname."', '".$email."', '".$mobile."', '".$phone."',
 '".$city."', '".$area."', '".$pincode."', '".$address."', '".$latitude."', '".$longitude."');");


$msg = 'Record has been added. <div style="Padding:20px 0 0 0;"><a href="">Go Back to tutorial</a></div>';
} else {
$msg = 'Record already exist. <div style="Padding:20px 0 0 0;"><a href="">Go Back to tutorial</a></div>';
}


}
echo $msg;
}
	}
	else {
echo "No file selected <br />";
}
	
	
}

?>