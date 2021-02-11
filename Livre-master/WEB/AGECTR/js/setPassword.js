$(document).ready(function() {

var tempPass;

  $(document).on("click", ".setPassword", function() {

    var nomUser = $(event.target)
      .closest("tr")
      .find($("td:nth-child(2)"))
      .text();

    $("#userName2").text(nomUser + " ?");

    id = $(this).closest('tr').attr('id');

    $("#idPassword").val(id);

tempPass = random_password_generate(8, 12);
   $("#valeurMotdePasse").val(tempPass);
    $("#passwordModal").modal("show");

  });




  $(document).on("click", "#password", function() {

tempPass = $("#valeurMotdePasse").val();

      id = $("#idPassword").val();

    $.ajax({
      url: "php/setPassword.php",
      method: "POST",
      data: {
          id: id,tempPass:tempPass
        },
      success: function(data) {
        if (data == "success") {
          alert("Le mot de passe a été généré avec succès");
          $("#passwordModal").modal("hide");
          $('#tblCreationCompte').DataTable().ajax.reload();
        } else {
          alert(data);
        }
      }
    });
  });

  function random_password_generate(min, max) {
    var passwordChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz#@!%&()/";
    var randPwLen = Math.floor(Math.random() * (max - min + 1)) + min;
    var randPassword = Array(randPwLen).fill(passwordChars).map(function(x) {
      return x[Math.floor(Math.random() * x.length)]
    }).join('');
    return randPassword;
  }


});
