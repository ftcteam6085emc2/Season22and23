package org.firstinspires.ftc.teamcode;

//import AprilTagDetection;
//import AprilTagIdCode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import java.util.Locale;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.ArrayList;
import org.openftc.apriltag.AprilTagDetection;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

@Autonomous(name = "AutoSleeve")
public class Sample_AprilTag_ID_Code extends LinearOpMode {
    
    private double offset = 0.322;
    private double clawOpen = 0;
    private double clawClose = 0.2;
    
    private BNO055IMU imu;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor linearSlide;
    private Servo clawServo = null;
    private ElapsedTime runtime = new ElapsedTime();

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    double detectedID = 999.99;
    AprilTagIdCode.BlocksContext myDetector;
    int numberOfDetections;
    ArrayList<org.openftc.apriltag.AprilTagDetection> allDetections = new ArrayList<org.openftc.apriltag.AprilTagDetection>();
    AprilTagDetection singleDetection;

    imu = hardwareMap.get(BNO055IMU.class, "imu");
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    parameters.mode = BNO055IMU.SensorMode.IMU;
    parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
    parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
    parameters.calibrationDataFile = "BNO055IMUCalibration.json";
    parameters.loggingEnabled = false;
    parameters.loggingTag = "IMU";
    parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
    
    imu.initialize(parameters);
    
    clawServo = hardwareMap.get(Servo.class, "clawServo");
    
    linearSlide = hardwareMap.get(DcMotor.class, "linearSlide");
    
    backLeft = hardwareMap.get(DcMotor.class, "backLeft");
    backRight = hardwareMap.get(DcMotor.class, "backRight");
    frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
    frontRight = hardwareMap.get(DcMotor.class, "frontRight");
    
    backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    
    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    
    linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    
    frontRight.setTargetPosition(0);
    frontLeft.setTargetPosition(0);
    backRight.setTargetPosition(0);
    backLeft.setTargetPosition(0);
    
    linearSlide.setTargetPosition(0);
    
    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    
    linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    
    frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    
    linearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    // Put initialization blocks here.
    // Create a pipeline/webcam object for AprilTag ID code detection. Edit configured webcam name as needed.  Use this myBlock in INIT section of OpMode, before startAprilTagDetector.
    myDetector = AprilTagIdCode.createAprilTagDetector(hardwareMap, "Webcam 1");
    // Begin operating camera/stream/pipeline for AprilTag
    // detection. Must specify a resolution supported by the camera;
    // edit these default values as desired. Use this myBlock
    // in INIT section of OpMode, after createAprilTagDetector.
    AprilTagIdCode.startAprilTagDetector(myDetector, 640, 480);
    waitForStart();
    imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        // Put loop blocks here.
        // Provide the data from any and all detetected AprilTags. Use this myBlock anywhere in the OpMode, after startAprilTagDetector.
        allDetections = AprilTagIdCode.getAllDetections(myDetector);
        // Provide the number of detections in the current batch. Use this myBlock anywhere in the OpMode, after getAllDetections.
        numberOfDetections = AprilTagIdCode.getHowManyDetections(allDetections);
        if (numberOfDetections == 0) {
          telemetry.addData("No detectons", "KEEP LOOKING");
        } else if (numberOfDetections >= 2) {
          telemetry.addData("Multiple detections", "WAIT FOR ONE ONLY");
        } else {
          // Provide the data from only the first detection in the current batch.  This myBlock is helpful when you know there's only one AprilTag detected.  Use it anywhere, after getAllDetections. This will crash if the input detections list is empty.
          singleDetection = AprilTagIdCode.getOneDetection(allDetections, 0);
          // Provide the AprilTag ID code from the designated detection. Use this myBlock anywhere, after getOneDetection.
          detectedID = AprilTagIdCode.getID(singleDetection);
          telemetry.addData("Detected AprilTag ID code", detectedID);
        }
        
        if (detectedID == 1) {

        } else if (detectedID == 2) {
          
        } else if (detectedID == 3) {
        
        }else {
          telemetry.addData("No detectons", "KEEP LOOKING");
          MoveSlide(1000, 1);
        }
        telemetry.update();
      }
    }
  }
  
    private void DriveStraight(double power) {
        frontRight.setPower(power);
        frontLeft.setPower(power);
        backRight.setPower(power);
        backLeft.setPower(power);
    }
    
    private void slideStraight(double power) {
        linearSlide.setPower(power);
    }

    private void StopDriving() {
        DriveStraight(0);
    }

    private void DriveStraightDistance(int distance, double power) {
        telemetry.update();
        
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + distance);
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + distance);
        backRight.setTargetPosition(backRight.getCurrentPosition() + distance);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + distance);
        
        DriveStraight(power);
        
        while ((frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy() && frontLeft.isBusy()) && opModeIsActive()) {
            idle();
        }

        StopDriving();
    }
    
    private void MoveSlide(int distance, double power) {
        telemetry.update();
        
        linearSlide.setTargetPosition(linearSlide.getCurrentPosition() + distance);
        
        slideStraight(power);
        
        while (linearSlide.isBusy() && opModeIsActive()) {
            idle();
        }

        StopDriving();
    }

    private void OpenClaw(){
        clawServo.setPosition(clawOpen + offset);
    }
    
    private void CloseClaw(){
        clawServo.setPosition(clawClose + offset);
    }

    private void Strafe(int distance, double power) {
        telemetry.update();

        frontRight.setTargetPosition(frontRight.getCurrentPosition() + distance);
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() - distance);
        backRight.setTargetPosition(backRight.getCurrentPosition() - distance);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + distance);

        DriveStraight(power);
        while ((frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy() && frontLeft.isBusy()) && opModeIsActive()) {
            idle();
        }

        StopDriving();
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees) {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
    
    void turnTo(double target){
        target *= -1;
        
        double lowPower = 0.35;
        double highPower = 0.5;
        
        double angle = -imu.getAngularOrientation().firstAngle;
        
        boolean lAxis = false;
        
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        if(angle <= -175 || angle >= 175){
            lAxis = true;
            angle += 360;
            target += 360;
        }
        
        if(angle < target + 5){
            while(angle < target + 5 && opModeIsActive()){
                frontLeft.setPower(-highPower);
                backLeft.setPower(-highPower);
                frontRight.setPower(highPower);
                backRight.setPower(highPower);
                
                angle = imu.getAngularOrientation().firstAngle;
                
                if(lAxis){
                    angle += 360;
                }
                
                telemetry.addData("Angle", angle);
                telemetry.update();
            }
            while(angle > target + 5 && opModeIsActive()){
                frontLeft.setPower(lowPower);
                backLeft.setPower(lowPower);
                frontRight.setPower(-lowPower);
                backRight.setPower(-lowPower);
                
                angle = imu.getAngularOrientation().firstAngle;
                
                if(lAxis){
                    angle += 360;
                }
                
                telemetry.addData("Angle", angle);
                telemetry.update();
            }
        }else{
            while(angle > target - 5 && opModeIsActive()){
                frontLeft.setPower(highPower);
                backLeft.setPower(highPower);
                frontRight.setPower(-highPower);
                backRight.setPower(-highPower);
                
                angle = imu.getAngularOrientation().firstAngle;
                
                if(lAxis){
                    angle += 360;
                }
                
                telemetry.addData("Angle", angle);
                telemetry.update();
            }
            while(angle < target - 5 && opModeIsActive()){
                frontLeft.setPower(-lowPower);
                backLeft.setPower(-lowPower);
                frontRight.setPower(lowPower);
                backRight.setPower(lowPower);
                
                angle = imu.getAngularOrientation().firstAngle;
                
                if(lAxis){
                    angle += 360;
                }
                
                telemetry.addData("Angle", angle);
                telemetry.update();
            }
        }
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
