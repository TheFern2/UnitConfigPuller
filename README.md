## UnitConfigPuller

A java utility to compare configuration files for equipment for Allen Bradley. The scenario is as follows. In a configuration file you can have tags to load default parameters when commissioning an equipment or system. This utility can compare a few tags to figure out which configuration file is loaded in the PLC.

## Instructions
Change the settings.ini communications settings for your Allen Bradley Controllogix PLC.
Add your configuration sections in the ConfigurationTags.ini

[Name of the configuration file]
tag01 = MyTag01|0.55
tag02 = Program:MyTag02|1

The config file name inside brackets is whatever you want to output to the log, i.e. Bottle Equipment 2000. The config file name will only output if the tags inside that section compare to TRUE. tag01, tag02 are arbitrary names they can be named to anything.

## Dependencies
Make sure JDK is installed on the machine running this utility
- EtherIP
- Ini4j

## TODO
- Add commands to tags such as MyTag01|0.55|log to append to file name
- More commands such as range, i.e. |8.55-9.5|
- Multiple MyTag01|6.808|7.55|7.88
