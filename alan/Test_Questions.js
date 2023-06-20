intent('Can you give tips on Safety gear?', p => {
    p.play('Sure, Always wear the right protective gear like gloves, safety glasses, and sturdy shoes when handling packaging materials or using machinery');
});

intent('Can you give more tips on lifting?', p => {
    p.play('Yes, Lift heavy objects properly to avoid hurting your back',
           'Bend your knees, keep your back straight or use equipment like forklifts or hand trucks when needed');
});

intent('How to optimize my warehouse workplace?', p => {
    p.play('Always keep pathways clear',
           'Remove obstacles from walkways and aisles to prevent accidents',
           'Regularly clear packaging materials and other debris to maintain a safe workspace');
});

intent('Can you tell me some forklift safety tips', p => {
    p.play('Wearing your seatbelt keeps you secure in case of sudden stops or accidents',
           'Before starting the forklift, inspect it for any visible damage or mechanical issues',
           'When loading or unloading materials, make sure the forklift is on stable ground',
           'Be Aware of Your Surroundings');
});

/*intent('', p => {
    p.play('');
});*/

intent('Can you give me all of the mojito ingredients?', p => {
    p.play('Sure, mojito has a total of 5 ingredients. You will need 60 ml white rum, 1 lime, 8-10 fresh mint leaves, 2 teaspoons sugar, soda water, crushed ice');
});

intent('(Can|Could) you tell me how to make a mojito?', 'Make a mojito', p => {
    p.play('Cut the lime into wedges and place them in a glass. Add the mint leaves and sugar to the glass. Muddle the lime, mint, and sugar gently using a muddler or the back of a spoon. This will release the flavors and aromas. Fill the glass with crushed ice and pour the rum over it. Stir well to mix the ingredients. Top up the glass with soda water and garnish with a sprig of mint. Serve chilled and enjoy your refreshing Mojito!');
});

intent('Can you give me all of the cuba libre ingredients?', p => {
    p.play('Of course, cuba libre has 4 ingredients. You will need 60 ml dark rum, 120-180 ml cola, lime wedges, ice cubes');
});

intent('Can you tell me how to make a cuba libre?', p => {
    p.play('Fill a highball glass with ice cubes. Squeeze the juice of a lime wedge into the glass and drop the wedge in. Pour the dark rum over the ice and lime. Top up the glass with cola and give it a gentle stir');
});

intent('Can you give me all of the pina colada ingredients?', p => {
    p.play('Yes I can. Pina colada has 5 components. They are 60 ml white rum, 60 ml pineapple juice, 60 ml coconut cream, pineapple wedges, ice cubes');
});

intent('Can you tell me how to make a pina colada?', p => {
    p.play('Fill a blender with ice cubes. Add the white rum, pineapple juice, and coconut cream to the blender. Blend everything until smooth and frothy. Pour the mixture into a glass and garnish with a pineapple wedge');
});

intent('What ingredients do I need to make pasta with minced meat', p => {
    p.play('300-500 grams of minced pork or beef, 1-2 onions, 2-4 cloves of garlic, 300 grams of pasta');
});

intent('Can you tell me how to make pasta with minced meat', p => {
    p.play('Set water in a pot to boil. Dice your onion and add it to a pan. Fry the onion for 5-8 minutes and add your minced meat into the pan. Cut up garlic and add to pan. When the water is boiling add your pasta. Fry meat until brown, boil pasta until al dente. Strain the pasta water and mix the meat and pasta.');
});

intent('What ingredients do I need to make a potato salad', p => {
    p.play('1 kilograms of potatoes, 2 onions. 500 grams mayonnaise, 500 grams doctors sausage, 1 can of peas, 1 can of yellow peas, 1 can of diced pickles');
});

intent('Can you tell me how to make a classic potato salad', p => {
    p.play('Peel your potatoes. Add them to a pot and boil them. While potatoes are boiling, dice your sausage and onions into small cubes. When potatoes are boiled, dice them into small cubes. Add everything to a big bowl/pot. Add salt according to taste. Mix. Chill in freezer for at least an hour');
});