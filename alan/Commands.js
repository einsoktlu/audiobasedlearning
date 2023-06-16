intent('Go to my library', p => {
    p.play('Opening your library');
    p.play({command: 'navigate', screen: 'library'});
});

intent('Go to media player', p => {
    p.play('Opening the media player');
    p.play({command: 'navigate', screen: 'media'}); 
});

intent('Go back', p => {
    p.play('Navigating back');
    p.play({command: 'navigate', screen: 'back'});
});

intent('pause', p => {
    p.play({command: 'pause', screen: 'media'});
    p.play('Pausing your audio file');
});

intent('resume', p => {
    p.play('Resuming your audio file');
    p.play({command: 'resume', screen: 'media'});
});