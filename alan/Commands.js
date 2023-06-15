intent('Go to my library', p => {
    p.play({command: 'navigate', screen: 'library'});
    p.play('Opening your library');
});