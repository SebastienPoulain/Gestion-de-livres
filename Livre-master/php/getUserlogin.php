<?php

require 'BD.php';

if (isset($_POST['email']) && isset($_POST['password'])) {
    $sql = "SELECT id, lastName, firstName, email, phoneNumber from customer where email = lower(:email) and password = :password;";
   $stmt = $conn->prepare($sql);
    $stmt->execute(array(':email' => $_POST['email'], ':password' => $_POST['password']));
    $user = $stmt->fetch();
	
	if ($user == false){

    $info = array(
    "id"=> null,
    "lastName"=> null,
    "firstName"=> null,
    "email"=> null,
    "phoneNumber"=> null);
	echo json_encode($info);
}else{
    echo json_encode($user);
    }
	
	
	
} else {
    echo json_encode('error');
}


$conn = null;
