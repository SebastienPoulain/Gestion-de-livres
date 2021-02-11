$(document).ready(function() {

  $(document).on("click", ".acceptDonation", function() {

    var id = $(this).closest('tr').attr('id');

    $("#id").val(id);

    $("#donationModal").modal("show");

  });

  $(document).on("click", "#donation", function() {

    var id = $("#id").val();

    $.ajax({
      url: "php/accepterDonation.php",
      method: "POST",
      data: {
        id: id
      },
      success: function(data) {
        if (data == "success") {
          alert("Le transfert de propriété à l'AGECTR a été fait avec succès");
          $("#donationModal").modal("hide");
          $('#tblDonations').DataTable().ajax.reload();
        } else {
          alert(data);
        }
      }
    });

  });

});