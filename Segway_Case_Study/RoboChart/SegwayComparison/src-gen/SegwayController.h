#ifndef ROBOCALC_CONTROLLERS_SEGWAYCONTROLLER_H_
#define ROBOCALC_CONTROLLERS_SEGWAYCONTROLLER_H_

#include "SegwayRP.h"
#include "RoboCalcAPI/Controller.h"
#include "DataTypes.h"

#include "BalanceSTM_P2.h"
#include "SpeedPID_S.h"
#include "RotationPID_S.h"

class SegwayController: public robocalc::Controller 
{
public:
	SegwayController(SegwayRP& _platform) : platform(&_platform){};
	SegwayController() : platform(nullptr){};
	
	~SegwayController() = default;
	
	void Execute()
	{
		balanceSTM_P2.execute();
		speedPID_S.execute();
		rotationPID_S.execute();
	}
	
	struct Channels
	{
		SegwayController& instance;
		Channels(SegwayController& _instance) : instance(_instance) {}
		
		EventBuffer* tryEmitLeftMotorVelocity(void* sender, std::tuple<double> args)
		{
			if(instance.balanceSTM_P2.canReceiveLeftMotorVelocity(args))
				instance.balanceSTM_P2.leftMotorVelocity_in.trigger(sender, args);
				
			return &instance.balanceSTM_P2.leftMotorVelocity_in;
		}
		
		EventBuffer* tryEmitRightMotorVelocity(void* sender, std::tuple<double> args)
		{
			if(instance.balanceSTM_P2.canReceiveRightMotorVelocity(args))
				instance.balanceSTM_P2.rightMotorVelocity_in.trigger(sender, args);
				
			return &instance.balanceSTM_P2.rightMotorVelocity_in;
		}
		
		EventBuffer* tryEmitAngle(void* sender, std::tuple<double> args)
		{
			if(instance.balanceSTM_P2.canReceiveAngle(args))
				instance.balanceSTM_P2.angle_in.trigger(sender, args);
				
			return &instance.balanceSTM_P2.angle_in;
		}
		
		EventBuffer* tryEmitGyroX(void* sender, std::tuple<double> args)
		{
			if(instance.balanceSTM_P2.canReceiveGyroX(args))
				instance.balanceSTM_P2.gyroX_in.trigger(sender, args);
				
			return &instance.balanceSTM_P2.gyroX_in;
		}
		
		EventBuffer* tryEmitGyroY(void* sender, std::tuple<double> args)
		{
			if(instance.balanceSTM_P2.canReceiveGyroY(args))
				instance.balanceSTM_P2.gyroY_in.trigger(sender, args);
				
			return &instance.balanceSTM_P2.gyroY_in;
		}
		
		EventBuffer* tryEmitGyroZ(void* sender, std::tuple<double> args)
		{
			if(instance.balanceSTM_P2.canReceiveGyroZ(args))
				instance.balanceSTM_P2.gyroZ_in.trigger(sender, args);
				
			return &instance.balanceSTM_P2.gyroZ_in;
		}
		
		EventBuffer* tryEmitAnewError(void* sender, std::tuple<double> args)
		{
			anewError_in.trigger(sender, args);
			return &anewError_in;
		}
		
		EventBuffer* tryEmitAdiff(void* sender, std::tuple<double> args)
		{
			adiff_in.trigger(sender, args);
			return &adiff_in;
		}
		
		EventBuffer* tryEmitAngleOutputE(void* sender, std::tuple<double> args)
		{
			if(instance.balanceSTM_P2.canReceiveAngleOutputE(args))
				instance.balanceSTM_P2.angleOutputE_in.trigger(sender, args);
				
			return &instance.balanceSTM_P2.angleOutputE_in;
		}
		
		EventBuffer* tryEmitSpeedOutputE(void* sender, std::tuple<double> args)
		{
			if(instance.balanceSTM_P2.canReceiveSpeedOutputE(args))
				instance.balanceSTM_P2.speedOutputE_in.trigger(sender, args);
				
			return &instance.balanceSTM_P2.speedOutputE_in;
		}
		
		EventBuffer* tryEmitSnewError(void* sender, std::tuple<double> args)
		{
			if(instance.speedPID_S.canReceiveSnewError(args))
				instance.speedPID_S.snewError_in.trigger(sender, args);
				
			return &instance.speedPID_S.snewError_in;
		}
		
		EventBuffer* tryEmitRotationOutputE(void* sender, std::tuple<double> args)
		{
			if(instance.balanceSTM_P2.canReceiveRotationOutputE(args))
				instance.balanceSTM_P2.rotationOutputE_in.trigger(sender, args);
				
			return &instance.balanceSTM_P2.rotationOutputE_in;
		}
		
		EventBuffer* tryEmitRdiff(void* sender, std::tuple<double> args)
		{
			if(instance.rotationPID_S.canReceiveRdiff(args))
				instance.rotationPID_S.rdiff_in.trigger(sender, args);
				
			return &instance.rotationPID_S.rdiff_in;
		}
		
		struct AnewError_t : public EventBuffer
		{
			THREAD_SAFE_ONLY(std::mutex _mutex;)
			std::tuple<double> _args;
			void* _sender = nullptr;
			void* getSender() override
			{
				THREAD_SAFE_ONLY(std::lock_guard<std::mutex> lock{_mutex};)
				return _sender;
			}
			
			void reset() override
			{
				THREAD_SAFE_ONLY(std::lock_guard<std::mutex> lock{_mutex};)
				_sender = nullptr;
			}
			
			void trigger(void* sender, std::tuple<double> args)
			{
				THREAD_SAFE_ONLY(std::lock_guard<std::mutex> lock{_mutex};)
				_args = args;
				_sender = sender;
			}
		} anewError_in;
		struct Adiff_t : public EventBuffer
		{
			THREAD_SAFE_ONLY(std::mutex _mutex;)
			std::tuple<double> _args;
			void* _sender = nullptr;
			void* getSender() override
			{
				THREAD_SAFE_ONLY(std::lock_guard<std::mutex> lock{_mutex};)
				return _sender;
			}
			
			void reset() override
			{
				THREAD_SAFE_ONLY(std::lock_guard<std::mutex> lock{_mutex};)
				_sender = nullptr;
			}
			
			void trigger(void* sender, std::tuple<double> args)
			{
				THREAD_SAFE_ONLY(std::lock_guard<std::mutex> lock{_mutex};)
				_args = args;
				_sender = sender;
			}
		} adiff_in;
	};
	
	Channels channels{*this};
	
	SegwayRP* platform;
	BalanceSTM_P2_StateMachine<SegwayController> balanceSTM_P2{*platform, *this, &balanceSTM_P2};
	SpeedPID_S_StateMachine<SegwayController> speedPID_S{*platform, *this, &speedPID_S};
	RotationPID_S_StateMachine<SegwayController> rotationPID_S{*platform, *this, &rotationPID_S};
};

#endif
