<?php

include "BD.php";

if (isset($_POST["id"])) {
    $id = $_POST["id"];
    $valide = 1;

    $sql = "UPDATE concession set valide = :valide where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':valide' => $valide,':id' => $id));
    echo "success";
} else {
    echo "error";
}
$conn = null;
