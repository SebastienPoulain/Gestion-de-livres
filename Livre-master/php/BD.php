<?php
$servername = "localhost";
$username = "1660689";
$password = "1660689";
$dbname = "420617ri_equipe-2";



try
{
  $conn = new PDO("mysql:host=$servername;dbname=$dbname",$username,$password);
  $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
  $connState = "Connected successfully";
  
}

catch(PDOException $e)
{
  $connState = "Connection failed: " . $e->getMessage();
}


