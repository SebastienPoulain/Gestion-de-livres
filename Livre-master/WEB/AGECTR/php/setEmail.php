<?php

include "BD.php";

if (isset($_POST["id"]) && isset($_POST["email"])) {
    $id = $_POST["id"];
    $email = $_POST["email"];

    $stmtExist = $conn->prepare("SELECT count(*) from customer where email = LOWER(:email)");
      $stmtExist->execute(array(':email' => $email));
      $emailCtr = $stmtExist->fetchColumn();

      if($emailCtr == 0){
    $sql = "UPDATE  customer set email = :email  where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':email' => $email,':id' => $id));
    echo "success";
  }else{
    echo "L'adresse email entrée existe déjà";
  }
} else {
    echo "error";
}
$conn = null;
