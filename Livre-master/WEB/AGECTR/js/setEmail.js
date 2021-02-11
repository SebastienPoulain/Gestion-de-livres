$(document).ready(function() {

  var pat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

  $(document).on("click", ".setEmail", function() {

    var nomUser = $(event.target)
      .closest("tr")
      .find($("td:nth-child(2)"))
      .text();

    $("#userName").text(" "+ nomUser);

    id = $(this).closest('tr').attr('id');

    $("#idEmail").val(id);


    $("#emailModal").modal("show");

  });

  $(document).on("click", "#fermer", function() {

  $("#addrEmail").val("");

  });



  $(document).on("click", "#email", function() {
    email = $("#addrEmail").val();

    if(email == "" || !pat.test(email)){
      alert("Vous devez entrer une adresse courriel valide");
    } else{

    id = $("#idEmail").val();

    $.ajax({
      url: "php/setEmail.php",
      method: "POST",
      data: {
        id: id,email:email
      },
      success: function(data) {
        if (data == "success") {
          alert("L'adresse courriel a été associée avec succès");
          $("#addrEmail").val("");
          $("#emailModal").modal("hide");
          $('#tblCreationCompte').DataTable().ajax.reload();
        } else {
          alert(data);
        }
      }
    });
}
  });

});
