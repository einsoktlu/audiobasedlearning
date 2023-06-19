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
    if (p.visual.screen === "media") {
        p.play({command: 'pause'});
        p.play('Pausing your audio file');
    } else {
        p.play('Sorry, you can only use this command on the media player');
    }
});

intent('$(PLAY play|resume)', p => {
    if (p.visual.screen === "media") {
        switch (p.PLAY.value) {
            case 'play':
                p.play('Playing your audio file');
                break;
            case 'resume':
                p.play('Resuming your audio file');
                break;
        }

        p.play({command: 'play'});
    } else {
        p.play('Sorry, you can only use this command on the media player');
    }
});
