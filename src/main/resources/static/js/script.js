const btn = document.getElementById('btn-hour');
const list = document.getElementById('timeslots');

// Set initial display state (optional)
list.style.display = 'none';  // Hide timeslots by default

btn.addEventListener('click', () => {
  // Toggle visibility of timeslots on click
  if (list.style.display === 'none') {
    list.style.display = 'grid'; // Or any desired display style
  } else {
    list.style.display = 'none';
  }
});