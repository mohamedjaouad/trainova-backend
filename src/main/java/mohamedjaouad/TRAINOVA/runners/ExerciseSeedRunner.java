package mohamedjaouad.TRAINOVA.runners;

import mohamedjaouad.TRAINOVA.entities.Exercise;
import mohamedjaouad.TRAINOVA.repositories.ExerciseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class ExerciseSeedRunner implements CommandLineRunner {

    private final ExerciseRepository exerciseRepository;

    public ExerciseSeedRunner(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Exercise> exercises = List.of(

                createExercise("Bench Press", "Petto", "Bilanciere", "Distensione su panca piana"),
                createExercise("Squat", "Gambe", "Bilanciere", "Accosciata profonda"),
                createExercise("Deadlift", "Schiena", "Bilanciere", "Sollevamento da terra"),
                createExercise("Lat Pulldown", "Schiena", "Cavi", "Trazioni al lat machine"),
                createExercise("Overhead Press", "Spalle", "Bilanciere", "Distensione sopra la testa"),
                createExercise("Bicep Curl", "Braccia", "Manubri", "Sollevamento alternato"),
                createExercise("Triceps Pushdown", "Braccia", "Cavi", "Estensioni al cavo"),
                createExercise("Leg Press", "Gambe", "Macchina", "Spinta con le gambe"),
                createExercise("Plank", "Core", "Corpo libero", "Isometria addominali"),
                createExercise("Pull Up", "Schiena", "Corpo libero", "Trazioni alla barra"),
                createExercise("Barbell Row", "Schiena", "Bilanciere", "Rematore con bilanciere"),
                createExercise("Romanian Deadlift", "Gambe", "Bilanciere", "Stacco rumeno"),
                createExercise("Front Squat", "Gambe", "Bilanciere", "Squat frontale"),
                createExercise("Dumbbell Bench Press", "Petto", "Manubri", "Distensione con manubri"),
                createExercise("Dumbbell Row", "Schiena", "Manubri", "Rematore con manubrio"),
                createExercise("Dumbbell Shoulder Press", "Spalle", "Manubri", "Spinta sopra la testa con manubri"),
                createExercise("Dumbbell Lunge", "Gambe", "Manubri", "Affondi con manubri"),
                createExercise("Goblet Squat", "Gambe", "Kettlebell", "Accosciata con kettlebell al petto"),
                createExercise("Kettlebell Swing", "Gambe", "Kettlebell", "Slancio esplosivo dell'anca"),
                createExercise("Kettlebell Press", "Spalle", "Kettlebell", "Spinta sopra la testa con kettlebell"),
                createExercise("Kettlebell Lunge", "Gambe", "Kettlebell", "Affondo con kettlebell"),
                createExercise("Rack Pull", "Schiena", "Power Rack", "Stacco parziale dal rack"),
                createExercise("Rack Barbell Squat", "Gambe", "Power Rack", "Squat con bilanciere nel rack"),
                createExercise("Rack Bench Press", "Petto", "Power Rack", "Panca con supporti di sicurezza"),
                createExercise("Band Row", "Schiena", "Bande elastiche", "Rematore con elastico"),
                createExercise("Band Chest Press", "Petto", "Bande elastiche", "Spinta per il petto con elastico"),
                createExercise("Band Squat", "Gambe", "Bande elastiche", "Squat con elastico"),
                createExercise("Band Lateral Raise", "Spalle", "Bande elastiche", "Alzate laterali con elastico"),
                createExercise("Push Up", "Petto", "Corpo libero", "Piegamenti a terra"),
                createExercise("Bodyweight Lunge", "Gambe", "Corpo libero", "Affondi a corpo libero"),
                createExercise("Glute Bridge", "Gambe", "Corpo libero", "Ponte per i glutei"),
                createExercise("Crunch", "Core", "Corpo libero", "Crunch addominali"),


                createExercise("Incline Bench Press", "Petto", "Bilanciere", "Distensione su panca inclinata"),
                createExercise("Decline Bench Press", "Petto", "Bilanciere", "Distensione su panca declinata"),
                createExercise("Close Grip Bench Press", "Petto", "Bilanciere", "Distensione con presa stretta"),
                createExercise("Incline Dumbbell Press", "Petto", "Manubri", "Distensione inclinata con manubri"),
                createExercise("Decline Dumbbell Press", "Petto", "Manubri", "Distensione declinata con manubri"),
                createExercise("Dumbbell Fly", "Petto", "Manubri", "Croci con manubri su panca piana"),
                createExercise("Cable Fly", "Petto", "Cavi", "Croci ai cavi"),
                createExercise("Cable Crossover", "Petto", "Cavi", "Croci incrociate ai cavi dall'alto"),
                createExercise("Chest Press Machine", "Petto", "Macchina", "Distensione alla macchina per il petto"),
                createExercise("Pec Deck", "Petto", "Macchina", "Croci alla macchina pec deck"),
                createExercise("Diamond Push Up", "Petto", "Corpo libero", "Piegamenti con mani a rombo, focus tricipiti/petto"),
                createExercise("Incline Push Up", "Petto", "Corpo libero", "Piegamenti con mani rialzate"),
                createExercise("Dips", "Petto", "Corpo libero", "Dip alle parallele, focus petto basso"),
                createExercise("Kettlebell Floor Press", "Petto", "Kettlebell", "Distensione da terra con kettlebell"),
                createExercise("Rack Incline Bench Press", "Petto", "Power Rack", "Distensione inclinata nel rack"),
                createExercise("Band Fly", "Petto", "Bande elastiche", "Croci con elastico"),


                createExercise("Hip Thrust", "Gambe", "Bilanciere", "Spinta d'anca con bilanciere per i glutei"),
                createExercise("Sumo Deadlift", "Gambe", "Bilanciere", "Stacco con presa larga"),
                createExercise("Good Morning", "Gambe", "Bilanciere", "Flessione busto con bilanciere sulle spalle"),
                createExercise("Dumbbell Step Up", "Gambe", "Manubri", "Salita su rialzo con manubri"),
                createExercise("Dumbbell Sumo Squat", "Gambe", "Manubri", "Squat sumo con manubrio"),
                createExercise("Leg Extension", "Gambe", "Macchina", "Estensione gambe alla macchina, focus quadricipiti"),
                createExercise("Leg Curl", "Gambe", "Macchina", "Flessione gambe alla macchina, focus femorali"),
                createExercise("Hack Squat", "Gambe", "Macchina", "Squat alla macchina hack squat"),
                createExercise("Calf Raise Machine", "Gambe", "Macchina", "Sollevamento polpacci alla macchina"),
                createExercise("Wall Sit", "Gambe", "Corpo libero", "Isometria contro il muro"),
                createExercise("Step Up", "Gambe", "Corpo libero", "Salita su rialzo a corpo libero"),
                createExercise("Single Leg Glute Bridge", "Gambe", "Corpo libero", "Ponte glutei su una gamba"),
                createExercise("Kettlebell Goblet Lunge", "Gambe", "Kettlebell", "Affondo con kettlebell al petto"),
                createExercise("Kettlebell Deadlift", "Gambe", "Kettlebell", "Stacco da terra con kettlebell"),
                createExercise("Rack Front Squat", "Gambe", "Power Rack", "Squat frontale nel rack"),
                createExercise("Band Leg Press", "Gambe", "Bande elastiche", "Spinta gambe sdraiati con elastico"),
                createExercise("Band Glute Kickback", "Gambe", "Bande elastiche", "Slancio gluteo con elastico"),


                createExercise("T-Bar Row", "Schiena", "Bilanciere", "Rematore a T-bar"),
                createExercise("Pendlay Row", "Schiena", "Bilanciere", "Rematore esplosivo da terra"),
                createExercise("Single Arm Dumbbell Row", "Schiena", "Manubri", "Rematore a un braccio con manubrio"),
                createExercise("Renegade Row", "Schiena", "Manubri", "Rematore in plank con manubri"),
                createExercise("Seated Cable Row", "Schiena", "Cavi", "Rematore seduto al cavo basso"),
                createExercise("Straight Arm Pulldown", "Schiena", "Cavi", "Tirata a braccia tese al cavo alto"),
                createExercise("Assisted Pull Up Machine", "Schiena", "Macchina", "Trazioni assistite alla macchina"),
                createExercise("Row Machine", "Schiena", "Macchina", "Rematore alla macchina"),
                createExercise("Inverted Row", "Schiena", "Corpo libero", "Rematore orizzontale sotto una barra"),
                createExercise("Superman", "Schiena", "Corpo libero", "Estensione lombare a terra"),
                createExercise("Kettlebell Row", "Schiena", "Kettlebell", "Rematore con kettlebell"),
                createExercise("Rack Chin Up", "Schiena", "Power Rack", "Trazioni presa supina nel rack"),
                createExercise("Band Pull Apart", "Schiena", "Bande elastiche", "Apertura elastico per la schiena alta"),
                createExercise("Band Lat Pulldown", "Schiena", "Bande elastiche", "Trazioni con elastico ancorato in alto"),


                createExercise("Push Press", "Spalle", "Bilanciere", "Spinta sopra la testa con slancio delle gambe"),
                createExercise("Upright Row", "Spalle", "Bilanciere", "Tirata al mento con bilanciere"),
                createExercise("Lateral Raise", "Spalle", "Manubri", "Alzate laterali con manubri"),
                createExercise("Front Raise", "Spalle", "Manubri", "Alzate frontali con manubri"),
                createExercise("Arnold Press", "Spalle", "Manubri", "Spinta sopra la testa con rotazione"),
                createExercise("Rear Delt Fly", "Spalle", "Manubri", "Alzate per deltoide posteriore"),
                createExercise("Cable Lateral Raise", "Spalle", "Cavi", "Alzate laterali al cavo"),
                createExercise("Face Pull", "Spalle", "Cavi", "Tirata al volto per deltoidi posteriori"),
                createExercise("Shoulder Press Machine", "Spalle", "Macchina", "Distensione spalle alla macchina"),
                createExercise("Pike Push Up", "Spalle", "Corpo libero", "Piegamenti a V per le spalle"),
                createExercise("Handstand Push Up", "Spalle", "Corpo libero", "Piegamenti in verticale"),
                createExercise("Kettlebell Halo", "Spalle", "Kettlebell", "Rotazione kettlebell attorno alla testa"),
                createExercise("Rack Push Press", "Spalle", "Power Rack", "Push press con supporti di sicurezza"),
                createExercise("Band Face Pull", "Spalle", "Bande elastiche", "Face pull con elastico"),
                createExercise("Band Overhead Press", "Spalle", "Bande elastiche", "Spinta sopra la testa con elastico"),


                createExercise("Barbell Curl", "Braccia", "Bilanciere", "Curl bicipiti con bilanciere"),
                createExercise("Skull Crusher", "Braccia", "Bilanciere", "Estensioni tricipiti sdraiati con bilanciere"),
                createExercise("Hammer Curl", "Braccia", "Manubri", "Curl a martello con manubri"),
                createExercise("Overhead Triceps Extension", "Braccia", "Manubri", "Estensione tricipiti sopra la testa con manubrio"),
                createExercise("Concentration Curl", "Braccia", "Manubri", "Curl di concentrazione seduto"),
                createExercise("Cable Curl", "Braccia", "Cavi", "Curl bicipiti al cavo basso"),
                createExercise("Rope Pushdown", "Braccia", "Cavi", "Estensioni tricipiti con corda"),
                createExercise("Overhead Cable Extension", "Braccia", "Cavi", "Estensione tricipiti al cavo sopra la testa"),
                createExercise("Preacher Curl Machine", "Braccia", "Macchina", "Curl bicipiti alla panca Scott meccanizzata"),
                createExercise("Triceps Dip Machine", "Braccia", "Macchina", "Dip tricipiti alla macchina"),
                createExercise("Chin Up", "Braccia", "Corpo libero", "Trazioni presa supina, focus bicipiti"),
                createExercise("Bench Dips", "Braccia", "Corpo libero", "Dip tricipiti tra due panche"),
                createExercise("Kettlebell Curl", "Braccia", "Kettlebell", "Curl bicipiti con kettlebell"),
                createExercise("Rack Dips", "Braccia", "Power Rack", "Dip tricipiti su supporti del rack"),
                createExercise("Band Curl", "Braccia", "Bande elastiche", "Curl bicipiti con elastico"),
                createExercise("Band Triceps Extension", "Braccia", "Bande elastiche", "Estensioni tricipiti con elastico"),


                createExercise("Weighted Russian Twist", "Core", "Manubri", "Rotazione busto con manubrio"),
                createExercise("Dumbbell Side Bend", "Core", "Manubri", "Flessione laterale busto con manubrio"),
                createExercise("Cable Crunch", "Core", "Cavi", "Crunch in ginocchio al cavo alto"),
                createExercise("Woodchopper", "Core", "Cavi", "Rotazione diagonale del busto al cavo"),
                createExercise("Ab Machine Crunch", "Core", "Macchina", "Crunch alla macchina addominali"),
                createExercise("Mountain Climber", "Core", "Corpo libero", "Ginocchia al petto in appoggio plank"),
                createExercise("Leg Raise", "Core", "Corpo libero", "Sollevamento gambe da sdraiati"),
                createExercise("Russian Twist", "Core", "Corpo libero", "Rotazione busto da seduti"),
                createExercise("Side Plank", "Core", "Corpo libero", "Plank laterale"),
                createExercise("Bicycle Crunch", "Core", "Corpo libero", "Crunch incrociato a bicicletta"),
                createExercise("Hollow Hold", "Core", "Corpo libero", "Isometria a corpo cavo"),
                createExercise("Kettlebell Windmill", "Core", "Kettlebell", "Rotazione laterale con kettlebell sopra la testa"),
                createExercise("Hanging Leg Raise", "Core", "Power Rack", "Sollevamento gambe alla sbarra del rack"),
                createExercise("Band Pallof Press", "Core", "Bande elastiche", "Anti-rotazione con elastico")
        );

        Set<String> existingNames = exerciseRepository.findAll().stream()
                .map(Exercise::getName)
                .filter(name -> name != null)
                .map(name -> name.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());

        List<Exercise> missingExercises = exercises.stream()
                .filter(exercise -> !existingNames.contains(exercise.getName().toLowerCase(Locale.ROOT)))
                .toList();

        if (!missingExercises.isEmpty()) {
            exerciseRepository.saveAll(missingExercises);
        }
    }

    private Exercise createExercise(String name, String muscle, String equip, String desc) {
        Exercise e = new Exercise();
        e.setName(name);
        e.setMuscleGroup(muscle);
        e.setEquipment(equip);
        e.setDescription(desc);
        return e;
    }
}