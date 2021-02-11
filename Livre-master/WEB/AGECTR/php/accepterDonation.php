<?php

include "BD.php";

if (isset($_POST["id"])) {
    $id = $_POST["id"];
    $valide = 0;

    $sql = "UPDATE concession set valide = :valide where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':valide' => $valide,':id' => $id));

    $sql = "SELECT * from concession where id = :id";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':id' => $id));
    $concession = $stmt->fetch(PDO::FETCH_OBJ);

    $idCustomer = 0;
    $createdBy = "Mobile";
    $createdDate = date('Y-m-d H:i:s');
    $dateExpiration=date('Y-m-d H:i:s', strtotime('+1 year'));
    $valide = 1;

    $sql = "INSERT INTO concession (customerPrice, feesPercentage,sellingPrice,expireDate,idCustomer,idBook,createdBy,createdDate,valide,imgPath,localisation,annotation) values(:customerPrice,:feesPercentage,:sellingPrice,:expireDate,:idCustomer,:idBook,:createdBy,:createdDate,:valide,:imgPath,:localisation,:annotation)";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':customerPrice' => $concession->customerPrice,':feesPercentage' => $concession->feesPercentage,':sellingPrice' => $concession->sellingPrice,':expireDate' => $dateExpiration,':idCustomer' => $idCustomer,':idBook' => $concession->idBook,':createdBy' => $createdBy,':createdDate' => $createdDate,':valide' => $valide,':imgPath' => $concession->imgPath,':localisation' => $concession->localisation,':annotation' => $concession->annotation));

    echo "success";
} else {
    echo "error";
}
$conn = null;
