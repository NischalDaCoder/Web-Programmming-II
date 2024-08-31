document.addEventListener('DOMContentLoaded', function() {
    console.log('WatchAnime application loaded');
    
    
    document.querySelector('#search button').addEventListener('click', function() {
        const searchTerm = document.querySelector('#search input').value;
        alert('Searching for anime: ' + searchTerm);

        
    });

    
    document.querySelectorAll('.anime-card').forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.boxShadow = '0 0 10px #ff4081';
        });
        card.addEventListener('mouseleave', function() {
            this.style.boxShadow = 'none';
        });
    });
});