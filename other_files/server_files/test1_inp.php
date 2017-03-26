<?php
ob_start();
require_once 'dbconnect.php';

$st = $_POST['status'];
$tm = $_POST['time'];
$dev = $_POST['device'];
 $sql = "update proj set time=$tm ,status=$st WHERE id=$dev";
  if(mysqli_query($con1,$sql)){
    echo 'success';
  }
  else{
    echo 'failure';
  }
  mysqli_close($con1);
?>