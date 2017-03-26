<?php
ob_start();
require_once 'dbconnect.php';
$sql = "SELECT * FROM proj";
if ($result=mysqli_query($con1,$sql))
  {
  // Fetch one and one row
  while ($row1=mysqli_fetch_row($result))
    {
    $device =$row1[0];
    $time=$row1[1];
    $status =$row1[2];
 echo $time.'s'.$status.'n';   
}
  // Free result set
  mysqli_free_result($result);
}
?>