// MainActivity.kt
package com.example.task2 // Keep this package name consistent with your build.gradle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.task2.ui.theme.Task2Theme // Changed to match your package name
import kotlinx.coroutines.delay

// Define a data class for Recipe
data class Recipe(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: Int, // Using Int for drawable resource ID
    val ingredients: List<String>,
    val steps: List<String>
)

// Sample Recipe Data (26 recipes)
val allRecipes = listOf(
    Recipe(
        1,
        "Spaghetti Bolognese",
        "🍝 A classic Italian pasta dish with rich tomato sauce.",
        R.drawable.recipe1, // Changed to R.drawable.recipe1
        listOf(
            "200g spaghetti",
            "250g minced beef (or plant-based mince)",
            "1 onion, finely chopped",
            "2 cloves garlic, minced",
            "1 can chopped tomatoes",
            "2 tablespoons tomato paste",
            "1 teaspoon dried oregano",
            "1 teaspoon dried basil",
            "Salt and pepper to taste",
            "Olive oil for cooking",
            "Fresh basil and grated Parmesan for garnish"
        ),
        listOf(
            "Cook the spaghetti in salted boiling water according to package instructions. Drain and set aside.",
            "In a large pan, heat olive oil over medium heat. Sauté onions and garlic until soft and fragrant.",
            "Add the minced beef and cook until browned. Stir in the tomato paste, then add the chopped tomatoes.",
            "Season with oregano, basil, salt, and pepper. Simmer on low heat for about 15–20 minutes, stirring occasionally.",
            "Serve the sauce over the cooked spaghetti. Garnish with fresh basil and grated Parmesan."
        )
    ),
    Recipe(
        2,
        "Chicken Caesar Salad",
        "🥗 A fresh and crisp salad with grilled chicken.",
        R.drawable.recipe2, // Changed to R.drawable.recipe2
        listOf(
            "2 boneless, skinless chicken breasts",
            "1 tbsp olive oil",
            "Salt and black pepper (to taste)",
            "1 large romaine lettuce (chopped)",
            "½ cup Caesar dressing (store-bought or homemade)",
            "¼ cup grated Parmesan cheese",
            "1 cup croutons",
            "Optional: lemon wedges, anchovy fillets (for authentic flavor)"
        ),
        listOf(
            "Cook the chicken: Rub chicken breasts with olive oil, salt, and pepper. Grill or pan-fry over medium heat for 5–6 minutes per side, until cooked through. Let rest for a few minutes, then slice into strips.",
            "Prepare the lettuce: Wash and chop the romaine lettuce. Dry thoroughly (use a salad spinner if available).",
            "Assemble the salad: In a large bowl, toss the chopped lettuce with Caesar dressing. Add sliced chicken on top. Sprinkle with Parmesan cheese and croutons.",
            "Serve: Optionally, garnish with lemon wedges or anchovies. Serve chilled or at room temperature."
        )
    ),
    Recipe(
        3,
        "Vegetable Stir-fry",
        "? A quick, colorful dish with crisp-tender vegetables.",
        R.drawable.recipe3, // Changed to R.drawable.recipe3
        listOf(
            "1 cup broccoli florets",
            "1 bell pepper (red or yellow), sliced",
            "1 carrot, thinly sliced",
            "1/2 cup snow peas",
            "1/2 cup sliced mushrooms",
            "2 tablespoons vegetable oil",
            "2 cloves garlic, minced",
            "1 tablespoon ginger, grated",
            "2 tablespoons soy sauce",
            "1 tablespoon oyster sauce (optional for vegetarians)",
            "1 teaspoon sesame oil",
            "1 teaspoon cornstarch mixed in 2 tablespoons water (slurry)",
            "Salt and pepper to taste",
            "Sesame seeds for garnish (optional)"
        ),
        listOf(
            "Heat vegetable oil in a wok or large pan over medium-high heat. Add garlic and ginger and stir for a few seconds until aromatic.",
            "Add carrots and broccoli first and stir-fry for 2 minutes. Add bell pepper, snow peas, and mushrooms. Stir-fry for another 3–4 minutes.",
            "Pour in soy sauce, oyster sauce, and sesame oil. Mix well. Add the cornstarch slurry and stir until the sauce thickens and coats the vegetables.",
            "Season with salt and pepper as needed. Garnish with sesame seeds and serve hot with rice or noodles."
        )
    ),
    Recipe(
        4,
        "Chocolate Lava Cake",
        "🍫 A decadent dessert with a gooey, molten center.",
        R.drawable.recipe4, // Changed to R.drawable.recipe4
        listOf(
            "100g dark chocolate",
            "100g unsalted butter",
            "2 large eggs",
            "2 egg yolks",
            "60g granulated sugar",
            "1 tsp vanilla extract",
            "2 tbsp all-purpose flour",
            "Pinch of salt",
            "Cocoa powder (for dusting)",
            "Ice cream or berries for serving (optional)"
        ),
        listOf(
            "Preheat oven to 200°C (400°F). Grease and flour ramekins.",
            "Melt chocolate and butter together in a double boiler or microwave. Stir until smooth.",
            "In a separate bowl, whisk eggs, egg yolks, sugar, and vanilla until light and fluffy.",
            "Fold in the melted chocolate mixture into the egg mixture. Gently fold in flour and salt until just combined.",
            "Pour batter into prepared ramekins. Bake for 12-14 minutes, or until edges are set but center is still soft.",
            "Invert onto plates, dust with cocoa powder, and serve immediately with ice cream or berries."
        )
    ),
    Recipe(
        5,
        "Butter Chicken",
        "🧈 Creamy, flavorful chicken in a rich tomato sauce.",
        R.drawable.recipe5, // Changed to R.drawable.recipe5
        listOf(
            "500g boneless chicken (cubed)",
            "1 cup yogurt",
            "1 tbsp ginger-garlic paste",
            "1 tsp turmeric",
            "1 tsp red chili powder",
            "1 tsp garam masala",
            "3 tbsp butter",
            "1 cup tomato puree",
            "1/2 cup fresh cream",
            "1 tbsp kasuri methi (dried fenugreek leaves)",
            "Salt to taste",
            "Fresh coriander (for garnish)"
        ),
        listOf(
            "Marinate chicken in yogurt, turmeric, chili powder, and salt for 1 hour.",
            "Cook chicken in a pan until golden and set aside.",
            "In the same pan, melt butter and add ginger-garlic paste.",
            "Add tomato puree, salt, chili powder, and garam masala.",
            "Cook until oil separates, then stir in cream and kasuri methi.",
            "Add cooked chicken and simmer for 10 minutes.",
            "Garnish with cream and coriander. Serve hot with naan or rice."
        )
    ),
    Recipe(
        6,
        "Paella",
        "🥘 A vibrant Spanish rice dish with seafood and chicken.",
        R.drawable.recipe6, // Changed to R.drawable.recipe6
        listOf(
            "1 cup short-grain rice (e.g., bomba)",
            "2 tbsp olive oil",
            "200g chicken (cubed)",
            "200g shrimp or prawns",
            "1 onion (finely chopped)",
            "2 garlic cloves (minced)",
            "1 bell pepper (chopped)",
            "1/2 cup peas",
            "2 ½ cups chicken broth",
            "A pinch of saffron threads",
            "1 tsp smoked paprika",
            "Salt and pepper to taste",
            "Lemon wedges (for serving)"
        ),
        listOf(
            "Soak saffron in warm broth and set aside.",
            "Heat oil in a wide pan, sauté chicken until browned. Remove and set aside.",
            "In the same pan, sauté onion, garlic, and bell pepper.",
            "Stir in rice, smoked paprika, and saffron broth.",
            "Add chicken back to the pan and simmer uncovered for 10 minutes.",
            "Add peas and place shrimp on top. Cook for another 10 minutes until rice is tender.",
            "Let it rest, garnish with lemon wedges, and serve."
        )
    ),
    Recipe(
        7,
        "Burrito",
        "🌯 A hearty and customizable Mexican wrap.",
        R.drawable.recipe7, // Changed to R.drawable.recipe7
        listOf(
            "1 large flour tortilla",
            "1/2 cup cooked rice",
            "1/2 cup black beans (cooked)",
            "1/4 cup grilled chicken or beef (sliced)",
            "2 tbsp salsa",
            "2 tbsp sour cream",
            "1/4 cup shredded lettuce",
            "2 tbsp grated cheese",
            "Optional: guacamole, jalapeños, corn"
        ),
        listOf(
            "Warm the tortilla slightly to make it pliable.",
            "Spread rice evenly in the center of the tortilla.",
            "Layer with beans, meat, lettuce, cheese, and salsa.",
            "Add sour cream and optional toppings.",
            "Fold the bottom up, sides in, and roll tightly.",
            "Grill or toast slightly for a crispy finish.",
            "Cut in half and serve warm."
        )
    ),
    Recipe(
        8,
        "Buffalo Wings",
        "🍗 Crispy chicken wings coated in a spicy sauce.",
        R.drawable.recipe8, // Changed to R.drawable.recipe8
        listOf(
            "12 chicken wings",
            "1/4 cup hot sauce (e.g., Frank’s RedHot)",
            "2 tbsp unsalted butter",
            "1 tbsp white vinegar",
            "Salt and pepper to taste",
            "Oil for frying or baking",
            "Optional: celery sticks, ranch or blue cheese dip"
        ),
        listOf(
            "Preheat oven to 220°C (425°F) or heat oil in a deep fryer.",
            "Season chicken wings with salt and pepper.",
            "Bake or fry until crispy and fully cooked (about 25-30 mins).",
            "In a pan, melt butter, add hot sauce and vinegar. Stir well.",
            "Toss the cooked wings in the buffalo sauce until evenly coated.",
            "Serve hot with celery sticks and dipping sauce."
        )
    ),
    Recipe(
        9,
        "Glazed Donuts",
        "🍩 Sweet, fluffy fried dough with a sugary glaze.",
        R.drawable.recipe9, // Changed to R.drawable.recipe9
        listOf(
            "2¼ tsp (1 packet) active dry yeast",
            "¾ cup warm milk (about 110°F / 43°C)",
            "¼ cup granulated sugar",
            "2½ cups all-purpose flour",
            "¼ tsp salt",
            "¼ tsp ground nutmeg (optional)",
            "1 large egg",
            "¼ cup unsalted butter (softened)",
            "Oil for frying",
            "For the Glaze:",
            "1½ cups powdered sugar",
            "2–3 tbsp milk",
            "½ tsp vanilla extract"
        ),
        listOf(
            "In a small bowl, dissolve yeast in warm milk and let it sit for 5–10 minutes until foamy.",
            "In a large bowl, mix sugar, flour, salt, and nutmeg.",
            "Add the egg, softened butter, and yeast mixture. Mix until a soft dough forms.",
            "Knead the dough for 5–7 minutes until smooth and elastic.",
            "Place dough in a greased bowl, cover, and let rise for 1–1.5 hours or until doubled in size.",
            "Roll out the dough to about ½ inch thickness. Cut out donut shapes using a donut cutter or two round cutters.",
            "Place donuts on a tray, cover lightly, and let rise again for 30 minutes.",
            "Heat oil in a deep fryer or large pan to 350°F (175°C). Fry donuts for 1–2 minutes per side until golden.",
            "Drain on paper towels.",
            "For the glaze, mix powdered sugar, milk, and vanilla until smooth. Dip warm donuts in glaze and place on a rack to set."
        )
    ),
    Recipe(
        10,
        "Palak Paneer",
        "🥬 A classic Indian spinach and cottage cheese curry.",
        R.drawable.recipe10, // Changed to R.drawable.recipe10
        listOf(
            "200g paneer (cubed)",
            "3 cups spinach leaves (washed and chopped)",
            "1 medium onion (chopped)",
            "1 medium tomato (chopped)",
            "1 green chili",
            "1 tsp ginger-garlic paste",
            "1 tsp cumin seeds",
            "½ tsp turmeric powder",
            "½ tsp red chili powder",
            "½ tsp garam masala",
            "2 tbsp oil or ghee",
            "2 tbsp cream (optional)",
            "Salt to taste",
            "Water as needed"
        ),
        listOf(
            "Blanch the spinach: Boil spinach in water for 2 minutes, then transfer to cold water. Blend the spinach along with green chili into a smooth puree.",
            "Heat oil in a pan. Add cumin seeds and let them splutter.",
            "Add chopped onions and sauté until golden brown.",
            "Add ginger-garlic paste and sauté for a minute.",
            "Add chopped tomatoes and cook until soft.",
            "Add turmeric, red chili powder, and salt. Mix well.",
            "Pour in the spinach puree and cook for 5–6 minutes on medium flame.",
            "Add paneer cubes and stir gently. Cook for another 3–4 minutes.",
            "Add garam masala and cream (optional). Mix and simmer for 2 more minutes.",
            "Serve hot with roti, naan, or rice."
        )
    ),
    Recipe(
        11,
        "Pad Thai",
        "🍜 Sweet, sour, and savory Thai stir-fried noodles.",
        R.drawable.recipe11, // Changed to R.drawable.recipe11
        listOf(
            "200g flat rice noodles",
            "200g chicken, shrimp, or tofu (sliced)",
            "2 eggs",
            "1 cup bean sprouts",
            "2 tbsp chopped peanuts",
            "2–3 green onions (sliced)",
            "2 tbsp vegetable oil",
            "1 lime (cut into wedges)",
            "Fresh coriander (optional)",
            "For the sauce:",
            "3 tbsp fish sauce (or soy sauce for vegetarian)",
            "1 tbsp tamarind paste",
            "1 tbsp brown sugar",
            "1 tsp chili flakes (adjust to taste)",
            "2 tbsp water"
        ),
        listOf(
            "Soak rice noodles in warm water for 30 minutes or until soft. Drain and set aside.",
            "In a bowl, mix all sauce ingredients until well combined.",
            "Heat oil in a large pan or wok. Add chicken, shrimp, or tofu and stir-fry until cooked.",
            "Push protein to one side and crack the eggs on the other side. Scramble and cook fully.",
            "Add soaked noodles and pour the sauce over them. Toss everything together until the noodles are evenly coated and heated through.",
            "Add bean sprouts and green onions. Stir-fry for another minute.",
            "Serve hot topped with chopped peanuts, lime wedges, and coriander."
        )
    ),
    Recipe(
        12,
        "Phở Bò",
        "🍲 A fragrant Vietnamese beef noodle soup.",
        R.drawable.recipe12, // Changed to R.drawable.recipe12
        listOf(
            "For the broth:",
            "1 kg beef bones (marrow and knuckle)",
            "300g beef brisket or chuck",
            "1 onion (halved)",
            "1 piece of ginger (about 4 inches, halved)",
            "2 star anise",
            "1 cinnamon stick",
            "3 cloves",
            "1 tbsp coriander seeds",
            "1 tbsp fish sauce",
            "1 tsp sugar",
            "Salt to taste",
            "4–5 liters of water",
            "For the bowl:",
            "400g flat rice noodles (banh pho)",
            "200g raw beef sirloin or eye-round (thinly sliced)",
            "Fresh herbs (Thai basil, cilantro, mint)",
            "Bean sprouts",
            "Lime wedges",
            "Thinly sliced onions and green onions",
            "Hoisin sauce and Sriracha (optional)",
            "Chili slices (optional)"
        ),
        listOf(
            "Char the aromatics: Roast the halved onion and ginger over open flame or in a dry pan until slightly blackened.",
            "Prepare the broth: Parboil beef bones and brisket for 5 minutes to remove impurities. Rinse bones and meat thoroughly. In a large pot, add cleaned bones, brisket, charred onion, ginger, and all spices. Add water to cover and bring to a boil, then simmer gently for 3–4 hours (skimming foam as needed). After 2 hours, remove the brisket, let it cool, then slice thinly. Add fish sauce, sugar, and salt to taste.",
            "Prepare noodles and garnishes: Soak rice noodles in warm water, then cook according to package instructions. Arrange herbs, lime wedges, sprouts, chili slices, and sauces on a platter.",
            "Assemble the bowl: In a bowl, add noodles, slices of cooked brisket, and raw beef slices. Pour the hot broth over, which will cook the raw beef instantly. Top with onions and green onions.",
            "Serve: Let everyone customize with herbs, lime, and sauces to taste."
        )
    ),
    Recipe(
        13,
        "Fish Tacos",
        "🌮 Light and flavorful tacos with seasoned fish.",
        R.drawable.recipe13, // Changed to R.drawable.recipe13
        listOf(
            "For the fish:",
            "500g white fish fillets (like cod, tilapia, or haddock)",
            "1 tsp chili powder",
            "½ tsp cumin",
            "½ tsp paprika",
            "Salt and pepper to taste",
            "1 tbsp olive oil",
            "Juice of 1 lime",
            "For the slaw:",
            "2 cups shredded cabbage (red or green)",
            "¼ cup chopped cilantro",
            "2 tbsp mayonnaise or Greek yogurt",
            "1 tbsp lime juice",
            "Salt to taste",
            "For assembling:",
            "8 small corn or flour tortillas",
            "1 avocado (sliced)",
            "Salsa or pico de gallo (optional)",
            "Lime wedges",
            "Hot sauce (optional)"
        ),
        listOf(
            "Marinate the fish: In a bowl, mix olive oil, lime juice, chili powder, cumin, paprika, salt, and pepper. Coat fish fillets with the mixture and let it marinate for 15–20 minutes.",
            "Make the slaw: In another bowl, combine shredded cabbage, cilantro, mayo or yogurt, lime juice, and salt. Mix well and set aside.",
            "Cook the fish: Heat a non-stick pan over medium heat. Cook the fish for 3–4 minutes per side, or until flaky and cooked through. Remove and gently break into large chunks.",
            "Warm the tortillas: Heat tortillas on a skillet for 30 seconds per side or microwave them covered with a damp paper towel.",
            "Assemble the tacos: Add slaw to the tortilla, top with fish pieces, avocado slices, and salsa. Serve with lime wedges and hot sauce if desired."
        )
    ),
    Recipe(
        14,
        "Banana Pancakes",
        "🥞 Fluffy pancakes made with ripe bananas.",
        R.drawable.recipe14, // Changed to R.drawable.recipe14
        listOf(
            "2 ripe bananas (mashed)",
            "2 eggs",
            "1 cup all-purpose flour",
            "1 tbsp sugar (optional)",
            "1 tsp baking powder",
            "½ tsp baking soda",
            "½ tsp cinnamon (optional)",
            "¼ tsp salt",
            "¾ cup milk (dairy or plant-based)",
            "1 tsp vanilla extract",
            "Butter or oil for cooking"
        ),
        listOf(
            "Mash the bananas: In a large bowl, mash ripe bananas with a fork until smooth.",
            "Mix wet ingredients: Add eggs, milk, and vanilla extract to the mashed bananas. Whisk together until fully combined.",
            "Combine dry ingredients: In a separate bowl, mix flour, sugar (if using), baking powder, baking soda, cinnamon, and salt.",
            "Make the batter: Slowly add dry ingredients to the wet mixture. Stir until just combined (do not overmix). Let it rest for 5 minutes.",
            "Cook the pancakes: Heat a non-stick skillet or griddle over medium heat and add butter or oil. Pour about ¼ cup of batter per pancake. Cook until bubbles form on the surface and the edges look set (2–3 minutes), then flip and cook for another 1–2 minutes.",
            "Serve: Stack pancakes and serve warm with maple syrup, sliced bananas, or a dusting of powdered sugar."
        )
    ),
    Recipe(
        15,
        "Masala Omelette",
        "🍳 A spicy and flavorful Indian-style omelette.",
        R.drawable.recipe15, // Changed to R.drawable.recipe15
        listOf(
            "2 eggs",
            "1 small onion (finely chopped)",
            "1 small tomato (finely chopped)",
            "1 green chili (finely chopped)",
            "2 tbsp fresh coriander leaves (chopped)",
            "¼ tsp turmeric powder",
            "¼ tsp red chili powder",
            "Salt (to taste)",
            "1–2 tsp oil or ghee (for frying)"
        ),
        listOf(
            "Prepare the mixture: Crack the eggs into a bowl. Add chopped onion, tomato, green chili, coriander, turmeric, red chili powder, and salt. Beat everything together until well mixed.",
            "Cook the omelette: Heat oil or ghee in a non-stick pan over medium heat. Pour the egg mixture into the pan and spread it evenly. Cook for 2–3 minutes until the bottom is golden and set. Flip carefully and cook the other side for 1–2 minutes.",
            "Serve: Serve hot with toast, paratha, or enjoy as is. Optionally, squeeze a little lemon juice for a fresh zing."
        )
    ),
    Recipe(
        16,
        "Biryani",
        "🍽 A fragrant and rich rice dish with marinated meat.",
        R.drawable.recipe16, // Changed to R.drawable.recipe16
        listOf(
            "For Marination:",
            "500g chicken (or mutton), cleaned",
            "1 cup yogurt",
            "1 tbsp ginger-garlic paste",
            "1 tsp turmeric powder",
            "1 tsp red chili powder",
            "1 tsp garam masala",
            "Juice of 1 lemon",
            "Salt to taste",
            "For Rice:",
            "2 cups basmati rice",
            "4 cups water",
            "2–3 green cardamom",
            "1 black cardamom",
            "4 cloves",
            "1 small piece cinnamon",
            "1 bay leaf",
            "Salt to taste",
            "For Biryani Masala Layer:",
            "2 large onions (thinly sliced)",
            "2 tomatoes (chopped)",
            "1 tsp biryani masala (optional)",
            "2 green chilies (slit)",
            "½ cup fresh mint leaves",
            "½ cup coriander leaves",
            "3 tbsp oil or ghee",
            "Saffron strands soaked in 2 tbsp warm milk",
            "2 tbsp fried onions (optional garnish)"
        ),
        listOf(
            "Marinate the meat: In a bowl, mix all marination ingredients. Coat the meat well, cover, and marinate for at least 2 hours (overnight preferred).",
            "Cook the rice: Rinse and soak basmati rice for 30 minutes. Boil water with whole spices and salt. Add soaked rice and cook until 70% done. Drain and set aside.",
            "Prepare the masala: Heat oil/ghee in a pan, add sliced onions and sauté until golden brown. Add tomatoes, green chilies, mint, and coriander. Cook until tomatoes are soft and the oil separates. Add marinated meat and cook until the meat is 80% done (for mutton, you may need to pressure cook).",
            "Layering the biryani: In a heavy-bottomed pot, layer half the cooked rice, followed by the cooked meat masala. Add remaining rice on top. Drizzle saffron milk and a spoon of ghee over the top. Add fried onions if using.",
            "Dum cooking (steam): Cover with a tight-fitting lid or seal with dough. Cook on low flame for 20–25 minutes. Rest for 10 minutes before opening.",
            "Serve: Gently fluff the biryani and serve hot with raita or salan."
        )
    ),
    Recipe(
        17,
        "Apple Pie",
        "🥧 A classic American dessert with spiced apple filling.",
        R.drawable.recipe17, // Changed to R.drawable.recipe17
        listOf(
            "For the pie crust:",
            "2½ cups all-purpose flour",
            "1 cup (226g) unsalted butter (cold, cubed)",
            "1 tsp salt",
            "1 tbsp sugar",
            "6–8 tbsp ice water",
            "For the apple filling:",
            "6–7 medium apples (Granny Smith or a mix, peeled, cored, and sliced)",
            "¾ cup granulated sugar",
            "¼ cup brown sugar",
            "1 tsp ground cinnamon",
            "¼ tsp ground nutmeg",
            "2 tbsp all-purpose flour",
            "1 tbsp lemon juice",
            "2 tbsp unsalted butter (cut into small pieces)",
            "For brushing:",
            "1 egg (beaten, for egg wash)",
            "1 tbsp sugar (for sprinkling)"
        ),
        listOf(
            "Make the pie crust: In a large bowl, mix flour, sugar, and salt. Cut in the cold butter until the mixture resembles coarse crumbs. Gradually add ice water, 1 tbsp at a time, until the dough comes together. Divide the dough into two disks, wrap in plastic, and chill for at least 1 hour.",
            "Prepare the filling: In a bowl, toss the sliced apples with sugars, cinnamon, nutmeg, flour, and lemon juice. Let sit for 15–20 minutes to release juices.",
            "Roll and assemble: Preheat oven to 425°F (220°C). Roll out one dough disk and line a 9-inch pie pan. Add apple filling and dot with butter. Roll out the second dough disk and place over the top. Trim excess and crimp the edges. Cut slits in the top crust (or make a lattice). Brush with egg wash and sprinkle sugar.",
            "Bake: Bake at 425°F for 15 minutes. Reduce to 350°F (175°C) and bake for another 40–45 minutes or until golden and bubbly. Cover edges with foil if browning too fast.",
            "Cool and serve: Let cool for at least 2 hours before slicing. Serve with whipped cream or vanilla ice cream."
        )
    ),
    Recipe(
        18,
        "Pork Dumplings",
        "🥟 Savory pork and vegetable filled dumplings.",
        R.drawable.recipe18, // Changed to R.drawable.recipe18
        listOf(
            "For the filling:",
            "1 lb (450g) ground pork",
            "1 cup finely chopped napa cabbage",
            "2 green onions (finely sliced)",
            "1 tbsp soy sauce",
            "1 tbsp sesame oil",
            "1 tbsp rice vinegar",
            "1 tsp grated ginger",
            "1 garlic clove (minced)",
            "½ tsp salt",
            "¼ tsp black pepper",
            "For assembly:",
            "30 round dumpling wrappers",
            "Small bowl of water (for sealing)",
            "Optional for dipping sauce:",
            "2 tbsp soy sauce",
            "1 tbsp rice vinegar",
            "1 tsp sesame oil",
            "½ tsp chili flakes or chili oil"
        ),
        listOf(
            "Prepare the filling: In a large bowl, combine ground pork, chopped cabbage, green onions, soy sauce, sesame oil, vinegar, ginger, garlic, salt, and pepper. Mix thoroughly until sticky and well combined. Set aside.",
            "Assemble the dumplings: Place a dumpling wrapper on a clean surface. Add about 1 tsp of filling in the center. Dip your finger in water and wet the edge of the wrapper. Fold the wrapper over the filling to form a half-moon. Press the edges together to seal. Optional: pleat the edges for a traditional look. Repeat with remaining wrappers and filling.",
            "Cook the dumplings: To steam: Place dumplings in a steamer lined with parchment or cabbage leaves. Steam over boiling water for 10–12 minutes. To pan-fry (potstickers): Heat 1 tbsp oil in a skillet over medium heat. Place dumplings flat side down and cook until golden brown (2–3 minutes). Add ¼ cup water, cover, and steam until water evaporates and dumplings are cooked through (5–6 minutes).",
            "Serve: Mix dipping sauce ingredients in a small bowl. Serve hot dumplings with dipping sauce on the side."
        )
    ),
    Recipe(
        19,
        "California Roll",
        "🍣 A popular inside-out sushi roll.",
        R.drawable.recipe19, // Changed to R.drawable.recipe19
        listOf(
            "For the sushi rice:",
            "2 cups sushi rice (short-grain)",
            "2¼ cups water",
            "¼ cup rice vinegar",
            "1½ tbsp sugar",
            "½ tsp salt",
            "For the filling:",
            "1 avocado (ripe but firm, sliced)",
            "1 cucumber (peeled, julienned)",
            "6–8 imitation crab sticks (or real cooked crab meat, shredded)",
            "Others:",
            "5–6 sheets of nori (seaweed)",
            "Toasted sesame seeds (optional)",
            "Bamboo sushi mat (wrapped in plastic wrap)",
            "Soy sauce, pickled ginger and wasabi (for serving)"
        ),
        listOf(
            "Prepare the sushi rice: Rinse the sushi rice under cold water until the water runs clear. Cook the rice with 2¼ cups of water (rice cooker or stovetop). While hot, gently mix rice vinegar, sugar, and salt into the rice. Let it cool to room temperature (do not refrigerate).",
            "Prepare the ingredients: Slice avocado and cucumber into thin strips. Shred or slice the imitation crab sticks.",
            "Assemble the roll: Place a nori sheet (shiny side down) on the bamboo mat. Wet your hands and spread a thin, even layer of sushi rice over the nori. Sprinkle sesame seeds on top (optional). Flip the nori over so the rice side is down (for inside-out roll). Place crab, cucumber, and avocado in a line across the center. Roll the sushi: Use the mat to gently roll from the bottom, pressing tightly to form a cylinder. Keep rolling until sealed. Use a little water to help seal the edge if needed.",
            "Cut and serve: Slice the roll into 6–8 pieces using a sharp, damp knife. Serve with soy sauce, wasabi, and pickled ginger."
        )
    ),
    Recipe(
        20,
        "Greek Salad",
        "🥗 A refreshing salad with feta, olives, and fresh vegetables.",
        R.drawable.recipe20, // Changed to R.drawable.recipe20
        listOf(
            "2 cups cherry tomatoes (halved) or 2 medium tomatoes (cut into chunks)",
            "1 cucumber (sliced or diced)",
            "1 red onion (thinly sliced)",
            "1 green bell pepper (sliced)",
            "½ cup Kalamata olives (pitted)",
            "½ cup feta cheese (cut into cubes or crumbled)",
            "1 tbsp fresh oregano or 1 tsp dried oregano",
            "For the dressing:",
            "3 tbsp extra virgin olive oil",
            "1 tbsp red wine vinegar",
            "½ tsp Dijon mustard (optional)",
            "Salt and black pepper to taste",
            "Juice of half a lemon (optional)"
        ),
        listOf(
            "Prepare the vegetables: Wash and cut the tomatoes, cucumber, onion, and bell pepper. Slice or crumble the feta cheese. Pit the olives if not already done.",
            "Make the dressing: In a small bowl or jar, whisk together olive oil, vinegar, mustard (if using), lemon juice, salt, and pepper.",
            "Combine and toss: In a large bowl, combine all the chopped vegetables and olives. Pour the dressing over the salad and gently toss.",
            "Add feta and oregano: Top with feta cheese and sprinkle with oregano. Serve immediately or chill for 15–20 minutes before serving for better flavor."
        )
    ),
    Recipe(
        21,
        "Double Cheese Burger",
        "🍔 A juicy burger with two patties and melted cheese.",
        R.drawable.recipe21, // Changed to R.drawable.recipe21
        listOf(
            "For the Patties:",
            "500g ground beef (80% lean, 20% fat)",
            "Salt and pepper to taste",
            "1 tsp garlic powder (optional)",
            "1 tsp onion powder (optional)",
            "For Assembly:",
            "4 slices cheddar cheese (or your favorite cheese)",
            "2 burger buns (toasted)",
            "Lettuce leaves",
            "Tomato slices",
            "Pickles",
            "Onion slices (raw or grilled)",
            "Ketchup, mayonnaise, mustard (as preferred)",
            "Butter (for toasting buns)"
        ),
        listOf(
            "Prepare the patties: Divide the ground beef into 4 equal portions. Gently shape each into a round patty, slightly wider than the buns. Season both sides with salt, pepper, and optional garlic/onion powder.",
            "Cook the patties: Heat a skillet or grill over medium-high heat. Cook the patties for about 2–3 minutes on one side until browned. Flip and immediately place a cheese slice on each patty. Cook for another 2–3 minutes until the cheese is melted and the patty is cooked through.",
            "Toast the buns: Spread a little butter on the inside of each bun. Toast on the skillet until golden and crispy.",
            "Assemble the burger: Bottom bun → Sauce (optional) → Lettuce → Tomato → 1 patty with cheese → Onion → Pickles → 2nd patty with cheese → Sauce (optional) → Top bun. Serve hot: Serve immediately with fries, wedges, or coleslaw on the side."
        )
    ),
    Recipe(
        22,
        "Tonkotsu Ramen",
        "🍜 Rich and creamy Japanese pork bone broth ramen.",
        R.drawable.recipe22, // Changed to R.drawable.recipe22
        listOf(
            "For the Broth:",
            "1 kg pork bones (neck, back, or femur)",
            "1 onion (halved)",
            "1 knob of ginger (sliced)",
            "4 garlic cloves",
            "1 leek or green onion (optional)",
            "Water (enough to cover bones)",
            "1 tbsp vegetable oil",
            "For the Tare (Seasoning Sauce):",
            "3 tbsp soy sauce",
            "1 tbsp mirin",
            "1 tbsp sake",
            "1 tsp sesame oil",
            "Toppings:",
            "Chashu pork slices (braised pork belly)",
            "Soft-boiled eggs (ajitsuke tamago)",
            "Bamboo shoots (menma)",
            "Green onions (chopped)",
            "Nori (seaweed)",
            "Corn or mushrooms (optional)",
            "For Noodles:",
            "Fresh ramen noodles (store-bought or homemade)"
        ),
        listOf(
            "Prepare the bones: Rinse the pork bones under cold water. Bring a pot of water to boil, add the bones, and boil for 10 minutes. Drain, rinse bones to remove impurities, and clean the pot.",
            "Make the broth: Return the cleaned bones to the pot. Add onion, garlic, ginger, and leek. Fill with fresh water to cover. Boil on high for at least 6–10 hours (longer = richer broth), skimming off scum as needed. Add more water as it reduces.",
            "Strain and season: Strain the broth through a fine mesh. In a clean pot, mix broth with tare ingredients and bring to a simmer. Taste and adjust saltiness if needed.",
            "Cook the noodles: Boil ramen noodles according to package instructions. Drain and set aside.",
            "Prepare toppings: Slice chashu pork. Cut soft-boiled eggs in half. Prepare any other toppings you'd like.",
            "Assemble the ramen: In a bowl: add noodles → pour hot broth → top with chashu, egg, bamboo shoots, green onions, and nori."
        )
    ),
    Recipe(
        23,
        "Garlic Bread",
        "🧄 Toasted bread with garlic and butter.",
        R.drawable.recipe23, // Changed to R.drawable.recipe23
        listOf(
            "1 baguette or Italian bread (sliced in half lengthwise)",
            "4–6 garlic cloves (minced)",
            "100 g (½ cup) unsalted butter (softened)",
            "1–2 tbsp fresh parsley (chopped)",
            "Salt to taste",
            "Optional:",
            "2 tbsp grated Parmesan cheese",
            "½ tsp chili flakes",
            "Mozzarella or shredded cheese for cheesy garlic bread"
        ),
        listOf(
            "Preheat oven to 180°C (350°F).",
            "Prepare garlic butter mixture: In a bowl, mix softened butter, minced garlic, parsley, and a pinch of salt. (Optional) Add Parmesan cheese or chili flakes for more flavor.",
            "Spread on bread: Cut the bread in halves or thick slices. Spread the garlic butter mixture generously on the cut side.",
            "Bake: Place the bread on a baking tray. Bake for 10–12 minutes until golden and crispy. For cheesy garlic bread, sprinkle mozzarella on top and bake until melted and bubbly (2–3 extra minutes).",
            "Serve: Cut into slices and serve warm."
        )
    ),
    Recipe(
        24,
        "Beef Stew",
        "🍲 A hearty and comforting beef and vegetable stew.",
        R.drawable.recipe24, // Changed to R.drawable.recipe24
        listOf(
            "500 g beef chuck (cut into cubes)",
            "2 tbsp vegetable oil",
            "1 onion (chopped)",
            "2–3 garlic cloves (minced)",
            "2 carrots (sliced)",
            "2 potatoes (cubed)",
            "1 celery stalk (sliced)",
            "2 tbsp tomato paste",
            "3 cups beef broth",
            "1 cup red wine (optional)",
            "1 tsp dried thyme",
            "1 tsp dried rosemary",
            "2 tbsp flour (for thickening)",
            "Salt and pepper to taste",
            "Fresh parsley (for garnish)"
        ),
        listOf(
            "Brown the beef: Heat oil in a large pot. Add beef cubes, season with salt and pepper, and sear on all sides until browned. Remove and set aside.",
            "Sauté aromatics: In the same pot, add onions and garlic. Sauté for 2–3 minutes until softened.",
            "Add tomato paste and flour: Stir in tomato paste and cook for 1–2 minutes. Sprinkle flour and stir to coat the veggies and form a thick base.",
            "Deglaze and simmer: Pour in wine (if using) and let it reduce slightly. Add beef broth and stir to combine, scraping the bottom of the pot.",
            "Add vegetables and beef: Add carrots, potatoes, celery, and browned beef back into the pot. Season with thyme and rosemary.",
            "Cook slowly: Cover and simmer on low heat for 1.5 to 2 hours, until beef is tender and stew is thickened.",
            "Serve: Garnish with chopped parsley and serve hot with bread or rice."
        )
    ),
    Recipe(
        25,
        "Pepperoni Pizza",
        "🍕 Classic pizza with savory pepperoni slices.",
        R.drawable.recipe25, // Changed to R.drawable.recipe25
        listOf(
            "For the Dough:",
            "2 ¼ tsp active dry yeast",
            "1 tsp sugar",
            "¾ cup warm water (110°F / 45°C)",
            "2 cups all-purpose flour",
            "1 tbsp olive oil",
            "½ tsp salt",
            "For the Sauce:",
            "½ cup tomato sauce",
            "1 tsp dried oregano",
            "½ tsp garlic powder",
            "½ tsp chili flakes (optional)",
            "Salt to taste",
            "For the Toppings:",
            "1 to 1½ cups shredded mozzarella cheese",
            "20–25 slices of pepperoni",
            "1 tbsp grated Parmesan (optional)",
            "Fresh basil leaves (optional)"
        ),
        listOf(
            "Prepare the dough: In a bowl, mix yeast, sugar, and warm water. Let it sit for 5–10 minutes until frothy. Add flour, olive oil, and salt. Knead until you get a smooth dough. Cover and let it rise for 1 hour in a warm place until doubled.",
            "Make the sauce: Mix tomato sauce with oregano, garlic powder, chili flakes, and salt. Set aside.",
            "Roll out the dough: Preheat oven to 475°F (245°C). Roll the dough into a 10–12 inch circle on a floured surface. Transfer to a baking sheet or pizza stone.",
            "Assemble the pizza: Spread a thin layer of sauce over the dough. Sprinkle mozzarella cheese evenly. Add pepperoni slices and Parmesan if using.",
            "Bake: Bake in the preheated oven for 12–15 minutes or until crust is golden and cheese is bubbly.",
            "Serve: Remove from oven, garnish with fresh basil if desired, slice, and enjoy!"
        )
    ),
    Recipe(
        26,
        "Chicken Tikka Masala",
        "🍛 Creamy, flavorful, and full of spices!",
        R.drawable.recipe26, // Changed to R.drawable.recipe26
        listOf(
            "For the Chicken Marinade:",
            "500g boneless chicken (breast or thigh), cut into cubes",
            "½ cup plain yogurt",
            "1 tbsp lemon juice",
            "1 tbsp ginger-garlic paste",
            "1 tsp turmeric powder",
            "1 tsp red chili powder",
            "1 tsp garam masala",
            "Salt to taste",
            "For the Masala Sauce:",
            "2 tbsp oil or butter",
            "1 onion, finely chopped",
            "1 tbsp ginger-garlic paste",
            "2 tomatoes, pureed",
            "1 tsp cumin",
            "1 tsp coriander powder",
            "1 tsp paprika or Kashmiri chili powder (for color)",
            "½ tsp turmeric",
            "1 tsp garam masala",
            "Salt to taste",
            "½ cup heavy cream (or fresh cream)",
            "Fresh coriander leaves for garnish"
        ),
        listOf(
            "Marinate the Chicken: Mix all marinade ingredients in a bowl and coat the chicken well. Cover and refrigerate for at least 1 hour (or overnight for better flavor).",
            "Cook the Chicken: Grill, pan-fry, or bake the marinated chicken pieces until slightly charred and cooked through. Set aside.",
            "Make the Masala Sauce: Heat oil or butter in a pan. Add chopped onions and sauté until golden. Add ginger-garlic paste and sauté for 1-2 minutes. Add tomato puree and cook until oil separates. Stir in cumin, coriander, turmeric, paprika, and salt. Cook for 2–3 minutes.",
            "Combine: Add the grilled chicken into the masala sauce. Add cream and simmer for 5–7 minutes until everything is well combined and creamy.",
            "Garnish and Serve: Sprinkle garam masala and chopped coriander. Serve hot with naan, roti, or steamed basmati rice."
        )
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Assuming TastyBitesTheme is defined in com.example.task2.ui.theme
            com.example.task2.ui.theme.Task2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Main navigation for the app
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") {
                            SplashScreen(navController = navController)
                        }
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

// Splash Screen Composable
@Composable
fun SplashScreen(navController: NavHostController) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(durationMillis = 1500))
        delay(2000) // Display splash for 2 seconds
        navController.navigate("home") {
            // Pop up to the start destination of the graph to avoid back stacking on splash screen
            popUpTo("splash") { inclusive = true }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(103.dp, 94.dp, 103.dp, 94.dp)
            .alpha(alpha.value)
    ) {
        // Logo (using user's specified logo.png)
        Box(
            modifier = Modifier
                .width(169.dp)
                .height(161.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(Color.Transparent)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Using R.drawable.logo
                contentDescription = "TastyBites Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
        // Title
        Text(
            text = "TastyBites",
            textAlign = TextAlign.Start,
            fontSize = 40.sp,
            color = Color(0xFFF8681D),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth() // Adjust to fill width if needed
        )
    }
}

// Home Screen Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredRecipes = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            allRecipes
        } else {
            allRecipes.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.description.contains(searchQuery, ignoreCase = true) ||
                        it.ingredients.any { ingredient -> ingredient.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    var showRecipePopup by remember { mutableStateOf(false) }
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(10.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(0.dp))
                .background(Color(0xFFF8F8F8))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Logo (using user's specified logo.png)
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(29.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.Transparent)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo), // Using R.drawable.logo
                    contentDescription = "TastyBites Small Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
            // Title
            Text(
                text = "TastyBites",
                textAlign = TextAlign.Start,
                fontSize = 24.sp,
                color = Color(0xFFF8681D),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f).padding(start = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(15.dp))
                .border(1.dp, Color(0xFFDFDFDF), RoundedCornerShape(15.dp)),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(24.dp)
                )
            },
            placeholder = {
                Text(
                    text = "Search recipes…",
                    fontSize = 16.sp,
                    color = Color(0xFF9E9E9E)
                )
            },
            // CORRECTED: Using OutlinedTextFieldDefaults.colors() for Material3
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Recipe Grid
        // Using LazyVerticalGrid for scrollable grid and responsive layout
        if (filteredRecipes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No recipes found.", fontSize = 18.sp, color = Color.Gray)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 170.dp), // Responsive grid cells
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .weight(1f) // Takes available space
                    .fillMaxWidth()
            ) {
                items(filteredRecipes) { recipe ->
                    RecipeCard(recipe = recipe) {
                        selectedRecipe = recipe
                        showRecipePopup = true
                    }
                }
            }
        }


        // Bottom Navigation (Home Icon placeholder)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(0.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFDFDFDF), RoundedCornerShape(0.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(0.dp))
                    .background(Color.Transparent)
            ) {
                // Using ic_home_icon.png as confirmed by user
                Icon(
                    painter = painterResource(id = R.drawable.ic_home_icon),
                    contentDescription = "Home Icon",
                    tint = Color(0xFFF8681D), // Assuming active state color
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // Recipe Popup Dialog
    if (showRecipePopup && selectedRecipe != null) {
        RecipePopup(recipe = selectedRecipe!!) {
            showRecipePopup = false
            selectedRecipe = null
        }
    }
}

// Recipe Card Composable
@Composable
fun RecipeCard(recipe: Recipe, onClick: (Recipe) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        modifier = Modifier
            .width(170.dp)
            .height(306.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
            .padding(12.dp)
            .clickable { onClick(recipe) }
    ) {
        // Recipe image
        Box(
            modifier = Modifier
                .width(146.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray) // Placeholder background
        ) {
            Image(
                painter = painterResource(id = recipe.imageUrl),
                contentDescription = recipe.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        // Recipe name
        Text(
            text = recipe.name,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color(0xFF333333),
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            modifier = Modifier.fillMaxWidth()
        )
        // Recipe description
        Text(
            text = recipe.description,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color(0xFF666666),
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Recipe Popup Screen Composable
@Composable
fun RecipePopup(recipe: Recipe, onClose: () -> Unit) {
    Dialog(onDismissRequest = onClose) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
            modifier = Modifier
                .fillMaxWidth() // Make it responsive
                .fillMaxHeight(0.8f) // Occupy max 80% of screen height
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(10.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
            ) {
                // Recipe Name
                Text(
                    text = recipe.name,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                // Close Icon
                IconButton(onClick = onClose, modifier = Modifier.size(30.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.DarkGray
                    )
                }
            }

            // Body (Scrollable content)
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                modifier = Modifier
                    .weight(1f) // Takes remaining space, enabling scroll
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp)
            ) {
                // Recipe Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray) // Placeholder background
                ) {
                    Image(
                        painter = painterResource(id = recipe.imageUrl),
                        contentDescription = recipe.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Ingredients Heading
                Text(
                    text = "Ingredients",
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                // Ingredients List
                recipe.ingredients.forEach { ingredient ->
                    Text(
                        text = "• $ingredient",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Steps Heading
                Text(
                    text = "Preparation Steps",
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                // Steps List
                recipe.steps.forEachIndexed { index, step ->
                    Text(
                        text = "${index + 1}. $step",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}


// --- Preview Composable (for Android Studio Preview pane) ---
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun SplashScreenPreview() {
    Task2Theme {
        // Provide a dummy NavController for preview purposes
        SplashScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun HomeScreenPreview() {
    Task2Theme {
        // Provide a dummy NavController for preview purposes
        HomeScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 480)
@Composable
fun RecipePopupPreview() {
    Task2Theme {
        RecipePopup(recipe = allRecipes.first()) {
            // Do nothing on close for preview
        }
    }
}