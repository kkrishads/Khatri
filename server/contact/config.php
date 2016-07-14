<?php
   $dbhost = 'localhost';
   $dbuser = 'root';
   $dbpass = '';
   $mysql_database="contact_info";
   $conn = mysql_connect($dbhost, $dbuser, $dbpass);
   if(! $conn )
   {
     die('Could not connect: ' . mysql_error());
   }
   mysql_select_db($mysql_database,$conn);

?>