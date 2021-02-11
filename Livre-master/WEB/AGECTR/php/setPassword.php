<?php


include "BD.php";

if (isset($_POST["id"])) {
    $id = $_POST["id"];

        $password1 = $_POST["tempPass"];
        $password2 = hash("SHA256", $password1);

    $sql = "UPDATE customer set password = :password where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':password' => $password2,':id' => $id));
    echo "success";
} else {
    echo "error";
}
$conn = null;
