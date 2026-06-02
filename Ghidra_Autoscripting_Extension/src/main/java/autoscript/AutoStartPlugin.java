// Package
package autoscript;


// Imports
import ghidra.app.plugin.ProgramPlugin;

import java.io.File;
import java.io.PrintWriter;
import java.util.Set;
import java.util.HashSet;

import generic.jar.ResourceFile;

import ghidra.app.events.ProgramOpenedPluginEvent;
import ghidra.framework.plugintool.PluginEvent;
import ghidra.framework.plugintool.PluginTool;
import ghidra.framework.plugintool.PluginInfo;
import ghidra.framework.plugintool.util.PluginStatus;
import ghidra.app.script.GhidraScript;
import ghidra.app.script.GhidraScriptProvider;
import ghidra.app.script.GhidraScriptUtil;
import ghidra.app.script.GhidraState;
import ghidra.program.model.listing.Program;
import ghidra.util.task.TaskMonitor;
import ghidra.util.Msg;



// Metadata AnnotationA/
@PluginInfo(
	status = PluginStatus.RELEASED,
	packageName = "AutoScript",
	category = "Scripting",
	shortDescription = "Auto-execute scripts on program open",
	description = "Grabs target script inside ghidra_scripts directory when Ghidra opens a program and launches it"
)


public class AutoStartPlugin extends ProgramPlugin {
    String scriptDirectory = System.getProperty("user.home") + "/ghidra_scripts";
    private final Set<String> executedScripts = new HashSet<>();

    public AutoStartPlugin(PluginTool tool) {
        super(tool);
    }

    @Override
    protected void programOpened(Program program) {
        String key = program.getDomainFile().getPathname();

        if (executedScripts.contains(key)) {
            return;
        }

        executedScripts.add(key);
        startScript(program);
    }

    @Override
    protected void programClosed(Program program) {
        String key = program.getDomainFile().getPathname();
        executedScripts.remove(key);
    }

    private void startScript(Program program) {
        try {
            File scriptFile = new File(scriptDirectory, "Autoscript.py");

            ResourceFile resourceFile = new ResourceFile(scriptFile);
            GhidraScriptProvider provider = GhidraScriptUtil.getProvider(resourceFile);

            PrintWriter writer = new PrintWriter(System.out);
            GhidraScript script = provider.getScriptInstance(resourceFile, writer);
            GhidraState state = new GhidraState(tool, tool.getProject(), program, null, null, null);
            
            Msg.info(this, "Auto-executing: " + scriptFile.getName());
            script.execute(state, TaskMonitor.DUMMY, writer);
        }

        catch(Exception e) {
            Msg.error(this, "Error executing auto script: " + e.getMessage());
        }
    }
}
