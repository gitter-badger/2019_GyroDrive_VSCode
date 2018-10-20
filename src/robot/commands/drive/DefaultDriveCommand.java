package robot.commands.drive;

import com.torontocodingcollective.commands.TDefaultDriveCommand;
import com.torontocodingcollective.commands.TDifferentialDrive;
import com.torontocodingcollective.oi.TStick;
import com.torontocodingcollective.oi.TStickPosition;
import com.torontocodingcollective.speedcontroller.TMotorSpeeds;

import robot.Robot;
import robot.oi.OI;
import robot.subsystems.DriveSubsystem;

/**
 * Default drive command for a drive base
 */
public class DefaultDriveCommand extends TDefaultDriveCommand {

	OI oi = Robot.oi;
	DriveSubsystem driveSubsystem = Robot.driveSubsystem;
	
	TDifferentialDrive differentialDrive = new TDifferentialDrive();
	
	public DefaultDriveCommand() {
		// The drive logic will be handled by the TDefaultDriveCommand
		// which also contains the requires(driveSubsystem) statement
		super(Robot.oi, Robot.driveSubsystem);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {

		// Check the driver controller buttons
		super.execute();

		// Enable turbo mode
		if (oi.getTurboOn()) {
			driveSubsystem.enableTurbo();
		}
		else {
			driveSubsystem.disableTurbo();
		}

		// Drive according to the type of drive selected in the 
		// operator input.
		TStickPosition leftStickPosition  = oi.getDriveStickPosition(TStick.LEFT);
		TStickPosition rightStickPosition = oi.getDriveStickPosition(TStick.RIGHT);
		
		TStick singleStickSide = oi.getSelectedSingleStickSide();
		
		TMotorSpeeds motorSpeeds;
		
		switch (oi.getSelectedDriveType()) {
		
		case SINGLE_STICK:
			TStickPosition singleStickPosition = rightStickPosition;
			if (singleStickSide == TStick.LEFT) {
				singleStickPosition = leftStickPosition;
			}
			motorSpeeds = differentialDrive.arcadeDrive(singleStickPosition);
			break;
		
		case TANK:
			motorSpeeds = differentialDrive.tankDrive(leftStickPosition, rightStickPosition);
			break;
		
		case ARCADE:
		default:
			motorSpeeds = differentialDrive.arcadeDrive(leftStickPosition, rightStickPosition);
			break;
		}

		driveSubsystem.setSpeed(motorSpeeds);
	}

	@Override
	protected boolean isFinished() {
		// The default command does not end
		return false;
	}
}
