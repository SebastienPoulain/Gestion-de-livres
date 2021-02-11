<?php

require 'BD.php';


echo "Salut 55555";


if(isset($_POST['id']) && isset($_POST['firstName']) && isset($_POST['lastName']) && isset($_POST['email']) && isset($_POST['password'])){

$sql = "SELECT COUNT(*) AS count FROM customer WHERE id = :id AND password = :password;";
$stmt = $conn->prepare($sql);
$stmt->execute(array(':id'=>$_POST['id'], ':password'=>$_POST['password']));
$count = $stmt->fetch();

if ($count['count'] == 1){
$id = $_POST['id'];
$sql = "UPDATE customer SET fistName = :firstName, lastname = :lastName, email = :email, phoneNumber = :phoneNumber WHERE id = $id;";
$stmt = $conn->prepare($sql);
$stmt->execute(array(':firstName'=>$_POST['firstName'], ':lastName'=>$_POST['lastName'],':email'=>$_POST['email']));
echo "success";
}
else echo "notfound";


 }
else echo "error";


$conn = null;
