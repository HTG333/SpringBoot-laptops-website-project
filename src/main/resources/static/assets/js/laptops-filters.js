
$(document).ready(function() {
	$('.delete-btn').on('click', function(e) {
		/* 	console.log("Button clicked") */
		e.preventDefault(); // Prevent the default behavior of the delete button
		var deleteUrl = $(this).attr("href");
		var deletedItem = $(this).closest('.item');
		var lapId = $(this).closest('.item').data('lapid');
		var SameItemInAllSections = $('.item[data-lapid="' + lapId + '"]');

		$.ajax({
			url: deleteUrl,
			type: 'get',
			success: function(response) {
				// If the deletion was successful, you can update the UI here
				// For example, remove the deleted element from the page
				deletedItem.remove(); // Assuming the item to be deleted is wrapped in an element with the 'item' class

				SameItemInAllSections.remove();
			},
			error: function(xhr, status, error) {
				console.log('Error deleting item: ' + error);
			}
		});
	});
});





$(document).ready(function() {
	$('#brand-filter-select').change(function() {
		var selectedFilter = $(this).val().trim().toLowerCase(); // Get the selected filter value

		$('.laptop-item2').each(function() {
			var laptopType = $(this).data('brand').trim().toLowerCase(); // Get the data-type attribute of each laptop item

			if (selectedFilter === 'all' || laptopType === selectedFilter) {
				$(this).show(); // Show the laptop item if it matches the selected filter or if 'all' is selected
			} else {
				$(this).hide(); // Hide the laptop item if it does not match the selected filter
			}
		});

	});
});



$(document).ready(function() {
	$('#cat-filter-select').change(function() {
		var selectedCategory = $(this).val().trim().toLowerCase();
		var filteredItems = $('.laptop-item1').filter(function() {
			var categoriesString = $(this).attr('data-category');
			if (categoriesString) {
				var categories = categoriesString.split(',').map(function(category) {
					return category.trim().toLowerCase();
				});
				return selectedCategory === 'all' || categories.includes(selectedCategory) || (selectedCategory === 'all' && categories.includes(''));
			} else {
				return selectedCategory === 'all';
			}
		});

		// Only show the first 100 filtered items
		$('.laptop-item1').hide(); // Hide all items first
		filteredItems.slice(0, 100).show(); // Show only the first 20 filtered items
	});
});



$(document).ready(function() {
	$('.selector').on('change', function() {
		var brandFilter = $('#brand-filter-select2').val().toLowerCase().trim();
		var catFilter = $('#cat-filter-select2').val().toLowerCase().trim();
		var budgetFilter = $('#budget-filter-select').val().toLowerCase().trim();

		$('.laptop-item2').each(function() {
			var laptopBrand = $(this).data('brand').toLowerCase().trim();
			var laptopCategory = $(this).data('category').toLowerCase().trim();
			var laptopBudget = $(this).data('budget').toLowerCase().trim();

			if ((brandFilter === 'all' || laptopBrand === brandFilter) &&
				(catFilter === 'all' || laptopCategory.split(',')[0]===catFilter)&&
				(budgetFilter === 'all' || laptopBudget.split(' ')[1] === budgetFilter)) {
				$(this).show(); // Display the laptop item
			} else {
				$(this).hide(); // Hide the laptop item
			}
		});
	});
});
