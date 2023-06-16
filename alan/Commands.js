intent('Go to my library', p => {
    p.play({command: 'navigate', screen: 'library'});
    p.play('Opening your library');
});

intent('Go to media player', p => {
    p.play({command: 'navigate', screen: 'media'});
    p.play('Opening the media player');
});

intent('Go back', p => {
    p.play({command: 'navigate', screen: 'back'});
    p.play('Navigating back');
});

intent('pause', p => {
    p.play({command: 'pause', screen 'media'})
    p.play('Pausing your audio file');
});

intent('resume', p => {
    p.play({command: 'resume', screen 'media'})
    p.play('Resuming your audio file');
});