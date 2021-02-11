<?php

include "BD.php";

if (isset($_POST["id"])) {
    $id = $_POST["id"];
    $etat = $_POST["etat"];

    $sql = "UPDATE concession set reserver = :reserver where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':reserver' => $etat,':id' => $id));
    echo "success";
} else {
    echo "error";
}
$conn = null;
