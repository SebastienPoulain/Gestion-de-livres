<?php
$servername = "206.167.140.56";
$username = "1660689";
$password = "1660689";
$port = "3306";
$dbname = "420617ri_equipe-2";

try {
    $conn = new PDO("mysql:host=$servername;port=$port;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $connState = "Connected successfully";
} catch (PDOException $e) {
    $connState = "Connection failed: " . $e->getMessage();
}
