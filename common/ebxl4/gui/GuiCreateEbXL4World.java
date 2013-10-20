package ebxl4.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiCreateEbXL4World extends GuiScreen {

  private GuiCreateWorld createWorldGui;
  private String settings = "";
  private String customizationTitle;

  public GuiCreateEbXL4World(GuiCreateWorld caller, String prams) {
    this.createWorldGui = caller;
    this.settings = prams;
  }

  public String getEbXL4GeneratorInfo() {
    return settings;
  }

  public void setEbXL4GeneratorInfo(String prams) {
    this.settings = prams;
  }

  public void initGui() {
    this.buttonList.clear();
    this.customizationTitle = I18n.getString("createWorld.customize.ebxl4.title");
    this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.getString("gui.done")));
    this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.getString("gui.cancel")));
  }

  protected void actionPerformed(GuiButton buttonAction) {
    if (buttonAction.id == 0) {
      this.createWorldGui.generatorOptionsToUse = this.getEbXL4GeneratorInfo();
      this.mc.displayGuiScreen(this.createWorldGui);
    } else if (buttonAction.id == 1) {
      this.mc.displayGuiScreen(this.createWorldGui);
    }
  }

  /**
   * Draws the screen and all the components in it.
   */
  public void drawScreen(int par1, int par2, float par3) {
    this.drawDefaultBackground();
    this.drawCenteredString(this.fontRenderer, this.customizationTitle, this.width / 2, 8, 16777215);
    int k = this.width / 2 - 92 - 16;
    super.drawScreen(par1, par2, par3);
  }
}
