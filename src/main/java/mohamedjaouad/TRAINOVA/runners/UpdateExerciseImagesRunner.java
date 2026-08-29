
package mohamedjaouad.TRAINOVA.runners;

import mohamedjaouad.TRAINOVA.entities.Exercise;
import mohamedjaouad.TRAINOVA.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(5)
public class UpdateExerciseImagesRunner implements CommandLineRunner {

    private final ExerciseRepository exerciseRepository;

    @Value("${cloudinary.name}")
    private String cloudName;

    public UpdateExerciseImagesRunner(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        String baseUrl = "https://res.cloudinary.com/" + cloudName + "/image/upload/";

        Map<String, String> exerciseImages = new HashMap<>();


        exerciseImages.put("Bench Press", "BenchPress.gif");
        exerciseImages.put("Dumbbell Bench Press", "DumbbellBenchPress.jpg");
        exerciseImages.put("Push Up", "PushUp.gif");
        exerciseImages.put("Rack Bench Press", "RackBenchPress.gif");
        exerciseImages.put("Incline Bench Press", "InclineBenchPress.gif");
        exerciseImages.put("Decline Bench Press", "DeclineBenchPress.jpg");
        exerciseImages.put("Close Grip Bench Press", "CloseGripBenchPress.jpg");
        exerciseImages.put("Incline Dumbbell Press", "InclineDumbbellPress.gif");
        exerciseImages.put("Decline Dumbbell Press", "DeclineDumbbellPress.gif");
        exerciseImages.put("Dumbbell Fly", "DumbbellFly.gif");
        exerciseImages.put("Cable Fly", "CableFly.jpg");
        exerciseImages.put("Cable Crossover", "CableCrossover.gif");
        exerciseImages.put("Chest Press Machine", "ChestPressMachine.gif");
        exerciseImages.put("Pec Deck", "PeckDeck.jpg");
        exerciseImages.put("Diamond Push Up", "DiamondPushUp.gif");
        exerciseImages.put("Incline Push Up", "InclinePushUp.gif");
        exerciseImages.put("Dips", "Dips.jpg");
        exerciseImages.put("Kettlebell Floor Press", "KettlebellFloorPress.jpg");
        exerciseImages.put("Rack Incline Bench Press", "RackInclineBenchPress.jpg");
        exerciseImages.put("Band Chest Press", "BandChestPress.jpg");
        exerciseImages.put("Band Fly", "BandFly.jpg");


        exerciseImages.put("Squat", "Squat.gif");
        exerciseImages.put("Front Squat", "FrontSquat.jpg");
        exerciseImages.put("Goblet Squat", "GobletSquat.jpg");
        exerciseImages.put("Glute Bridge", "GluteBridge.gif");
        exerciseImages.put("Good Morning", "GoodMorning.gif");
        exerciseImages.put("Hack Squat", "HackSquat.jpg");
        exerciseImages.put("Hip Thrust", "HipThrust.jpg");
        exerciseImages.put("Leg Curl", "LegCurl.gif");
        exerciseImages.put("Leg Extension", "LegExtension.jpg");
        exerciseImages.put("Leg Press", "LegPress.gif");
        exerciseImages.put("Romanian Deadlift", "RomanianDeadlift.jpg");
        exerciseImages.put("Single Leg Glute Bridge", "SingleLegGluteBridge.jpg");
        exerciseImages.put("Step Up", "StepUp.jpg");
        exerciseImages.put("Sumo Deadlift", "SumoDeadlift.jpg");
        exerciseImages.put("Wall Sit", "WallSit.jpg");
        exerciseImages.put("Rack Barbell Squat", "RackBarbellSquat.jpg");
        exerciseImages.put("Rack Front Squat", "RackFrontSquat.jpg");
        exerciseImages.put("Kettlebell Deadlift", "KettlebellDeadlift.jpg");
        exerciseImages.put("Kettlebell Goblet Lunge", "KettlebellGobletLunge.jpg");
        exerciseImages.put("Kettlebell Lunge", "KettlebellLunge.jpg");
        exerciseImages.put("Kettlebell Swing", "KettlebellSwing.gif");
        exerciseImages.put("Dumbbell Sumo Squat", "DumbbellSumoSquat.jpg");
        exerciseImages.put("Dumbbell Lunge", "DumbbellLunge.jpg");
        exerciseImages.put("Dumbbell Step Up", "DumbbellStepUp.gif");
        exerciseImages.put("Bodyweight Lunge", "BodyweightLunge.jpg");
        exerciseImages.put("Calf Raise Machine", "CalfRaiseMachine.jpg");
        exerciseImages.put("Band Squat", "BandSquat.gif");
        exerciseImages.put("Band Leg Press", "BandLegPress.jpg");
        exerciseImages.put("Band Glute Kickback", "BandGluteKickback.jpg");


        exerciseImages.put("Deadlift", "Deadlift.gif");
        exerciseImages.put("Lat Pulldown", "LatPulldown.gif");
        exerciseImages.put("Pull Up", "PullUp.gif");
        exerciseImages.put("Barbell Row", "BarbellRow.gif");
        exerciseImages.put("Dumbbell Row", "DumbbellRow.gif");
        exerciseImages.put("Rack Pull", "RackPull.jpg");
        exerciseImages.put("Band Row", "BandRow.jpg");
        exerciseImages.put("T-Bar Row", "T-BarRow.gif");
        exerciseImages.put("Pendlay Row", "PendlayRow.gif");
        exerciseImages.put("Single Arm Dumbbell Row", "SingleArmDumbbellRow.jpg");
        exerciseImages.put("Renegade Row", "RenegadeRow.gif");
        exerciseImages.put("Seated Cable Row", "SeatedCableRow.gif");
        exerciseImages.put("Straight Arm Pulldown", "StraightArmPullDown.gif");
        exerciseImages.put("Assisted Pull Up Machine", "AssistedPullUpMachine.jpg");
        exerciseImages.put("Row Machine", "RowMachine.gif");
        exerciseImages.put("Inverted Row", "InvertedRow.jpg");
        exerciseImages.put("Superman", "Superman.jpg");
        exerciseImages.put("Kettlebell Row", "KettlebellRow.gif");
        exerciseImages.put("Rack Chin Up", "RackChinUp.gif");
        exerciseImages.put("Band Pull Apart", "BandPullApart.jpg");
        exerciseImages.put("Band Lat Pulldown", "BandLatPulldown.jpg");


        exerciseImages.put("Overhead Press", "OverheadPress.gif");
        exerciseImages.put("Dumbbell Shoulder Press", "DumbbellShoulderPress.gif");
        exerciseImages.put("Kettlebell Press", "KettlebellPress.jpg");
        exerciseImages.put("Band Lateral Raise", "BandLateralRaise.jpg");
        exerciseImages.put("Push Press", "PushPress.gif");
        exerciseImages.put("Upright Row", "UprightRow.jpg");
        exerciseImages.put("Lateral Raise", "LateralRaise.jpg");
        exerciseImages.put("Front Raise", "FrontRaise.gif");
        exerciseImages.put("Arnold Press", "ArnoldPress.gif");
        exerciseImages.put("Rear Delt Fly", "RearDeltFly.jpg");
        exerciseImages.put("Cable Lateral Raise", "CableLateralRaise.jpg");
        exerciseImages.put("Face Pull", "FacePull.gif");
        exerciseImages.put("Shoulder Press Machine", "ShoulderPressMachine.gif");
        exerciseImages.put("Pike Push Up", "PikePushUp.gif");
        exerciseImages.put("Handstand Push Up", "HandstandPushUp.jpg");
        exerciseImages.put("Kettlebell Halo", "KettlebellHalo.jpg");
        exerciseImages.put("Rack Push Press", "RackPushPress.gif");
        exerciseImages.put("Band Face Pull", "BandFacePull.jpg");
        exerciseImages.put("Band Overhead Press", "BandOverheadPress.jpg");


        exerciseImages.put("Bicep Curl", "BicepCurl.gif");
        exerciseImages.put("Triceps Pushdown", "TricepsPushdown.gif");
        exerciseImages.put("Barbell Curl", "BarbellCurl.gif");
        exerciseImages.put("Skull Crusher", "SkullCrusher.gif");
        exerciseImages.put("Hammer Curl", "HammerCurl.gif");
        exerciseImages.put("Overhead Triceps Extension", "OverheadTricepsExtension.gif");
        exerciseImages.put("Concentration Curl", "ConcentrationCurl.jpg");
        exerciseImages.put("Cable Curl", "CableCurl.gif");
        exerciseImages.put("Rope Pushdown", "RopePushdown.gif");
        exerciseImages.put("Overhead Cable Extension", "OverheadCableExtension.gif");
        exerciseImages.put("Preacher Curl Machine", "PreacherCurlMachine.gif");
        exerciseImages.put("Triceps Dip Machine", "TricepsDipMachine.gif");
        exerciseImages.put("Chin Up", "ChinUp.gif");
        exerciseImages.put("Bench Dips", "BenchDips.gif");
        exerciseImages.put("Kettlebell Curl", "KettlebellCurl.gif");
        exerciseImages.put("Rack Dips", "RackDips.gif");
        exerciseImages.put("Band Curl", "BandCurl.jpg");
        exerciseImages.put("Band Triceps Extension", "BandTricepsExtension.jpg");


        exerciseImages.put("Plank", "Plank.jpg");
        exerciseImages.put("Crunch", "Crunch.gif");
        exerciseImages.put("Weighted Russian Twist", "WeightedRussianTwist.gif");
        exerciseImages.put("Dumbbell Side Bend", "DumbbellSideBend.gif");
        exerciseImages.put("Cable Crunch", "CableCrunch.gif");
        exerciseImages.put("Woodchopper", "Woodchopper.gif");
        exerciseImages.put("Ab Machine Crunch", "AbMachineCrunch.gif");
        exerciseImages.put("Mountain Climber", "MountainClimber.gif");
        exerciseImages.put("Leg Raise", "LegRaise.gif");
        exerciseImages.put("Russian Twist", "RussianTwist.jpg");
        exerciseImages.put("Side Plank", "SidePlank.jpg");
        exerciseImages.put("Bicycle Crunch", "BicycleCrunch.gif");
        exerciseImages.put("Hollow Hold", "HollowHold.jpg");
        exerciseImages.put("Kettlebell Windmill", "KettlebellWindmill.jpg");
        exerciseImages.put("Hanging Leg Raise", "HangingLegRaise.jpg");
        exerciseImages.put("Band Pallof Press", "BandPallofPress.jpg");

        System.out.println(" AGGIORNAMENTO IMMAGINI ESERCIZI ");

        int updated = 0;
        int notFound = 0;

        for (Map.Entry<String, String> entry : exerciseImages.entrySet()) {
            String exerciseName = entry.getKey();
            String fileName = entry.getValue();
            String imageUrl = baseUrl + fileName;

            Exercise exercise = exerciseRepository.findAll().stream()
                    .filter(e -> e.getName().equalsIgnoreCase(exerciseName))
                    .findFirst()
                    .orElse(null);

            if (exercise != null) {
                exercise.setImageUrl(imageUrl);
                exerciseRepository.save(exercise);
                updated++;

            } else {
                notFound++;

            }
        }


        System.out.println("Aggiornati: " + updated);
        System.out.println("Non trovati: " + notFound);
    }
}