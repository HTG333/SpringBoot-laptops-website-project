function shortenLink(link) {
    // Shorten the link if needed
    if (link.length > 20) {
        return link.substring(0, 20) + '...';
    }
    return link; // Return the original link if not longer than 20 characters
}

function removeCode(text) {
    // Remove code snippets enclosed in backticks (`) using a regex pattern
    return text.replace(/`[^`]*`/g, '');
}

function removeQuotes(text) {
    // Remove double quotation marks from the text
    return text.replace(/"/g, '');
}

$(document).ready(function () {
    var notesString =  $('div[data-notes]').data('notes'); // Get the value of laptop.notes
    console.log(notesString);
    if (notesString.trim() === '' || notesString === 'null') {
        // Handle null condition or empty string
        $('h4:contains("Extras")').hide(); // Hide the "Extras" heading
        return;
    }

    var notesArray = notesString.split(',');

    // Variable to store the detected link for the external button
    var externalLink = '';
    var categoryAdded = false; // Flag to track if category has been added

    // Iterate over the array elements and update the specified divs
    $.each(notesArray, function (index, value) {
        // Remove code snippets
        value = removeCode(value.trim());
        // Remove double quotes
        value = removeQuotes(value.trim());

        // Check if the value is a link and create a clickable link
        var linkRegex = /^(http|https):\/\/[\w\-]+(\.[\w\-]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?$/;
        if (linkRegex.test(value)) {
            externalLink = value;
        } else {
            // Add "category: " before the first element in the list
            if (index === 0 && !categoryAdded) {
                // Add the first element to the specified div
                $('#categorySpan').text(value);
                categoryAdded = true;
            }
            if (index === 2) {
                // Add the third element to the specified div
                $('#statSpan').text(value);
            }
        }
    });

    // Set the detected link as the href attribute of the external button or hide the button if no link found
    if (externalLink.trim() !== '') {
        $('#offerBtn').attr('href', externalLink);
    } else {
        $('#offerBtn').hide();
    }
});