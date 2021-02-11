<?php

include "BD.php";

if (isset($_POST["id"]) && isset($_POST["localisation"])) {
    $id = $_POST["id"];
    $localisation = $_POST["localisation"];

    $sql = "UPDATE  concession set localisation = :localisation  where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':localisation' => $localisation,':id' => $id));
    echo "success";
} else {
    echo "error";
}
$conn = null;
