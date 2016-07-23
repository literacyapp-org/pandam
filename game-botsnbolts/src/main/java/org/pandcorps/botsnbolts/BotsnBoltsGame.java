/*
Copyright (c) 2009-2016, Andrew M. Martin
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
conditions are met:

 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
   disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of Pandam nor the names of its contributors may be used to endorse or promote products derived from this
   software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/
package org.pandcorps.botsnbolts;

import java.util.*;

import org.pandcorps.botsnbolts.Player.*;
import org.pandcorps.core.*;
import org.pandcorps.game.*;
import org.pandcorps.game.actor.*;
import org.pandcorps.pandam.*;
import org.pandcorps.pandam.impl.*;
import org.pandcorps.pandax.tile.*;

public final class BotsnBoltsGame extends BaseGame {
    protected final static String TITLE = "Bots 'n Bolts";
    protected final static String VERSION = "0.0.1";
    protected final static String YEAR = "2016";
    protected final static String AUTHOR = "Andrew M. Martin";
    
    protected final static String RES = "org/pandcorps/botsnbolts/";
    private final static FinPanple2 ng = GuyPlatform.getMin(Player.PLAYER_X);
    private final static FinPanple2 xg = GuyPlatform.getMax(Player.PLAYER_X, Player.PLAYER_H);
    protected final static FinPanple2 og = new FinPanple2(17, 1);
    protected static Queue<Runnable> loaders = new LinkedList<Runnable>();
    private static PlayerImages voidImages = null;
    
    protected static PlayerContext pc = null;
    
    protected static TileMap tm = null;

    @Override
    protected final boolean isFullScreen() {
        return true;
    }
    
    @Override
    protected final void init(final Panroom room) throws Exception {
        final Pangine engine = Pangine.getEngine();
        engine.setTitle(TITLE);
        engine.setEntityMapEnabled(false);
        Imtil.onlyResources = true;
        if (loaders != null) {
            loaders.add(new Runnable() {
                @Override public final void run() {
                    loadResources();
                }});
        }
        Panscreen.set(new LogoScreen(BotsnBoltsScreen.class, loaders));
    }
    
    private final static void loadResources() {
        voidImages = loadPlayerImages("betabot", "Void");
        pc = new PlayerContext(new Profile(), org.pandcorps.pandax.in.ControlScheme.getDefaultKeyboard(), voidImages);
    }
    
    private final static PlayerImages loadPlayerImages(final String dir, final String name) {
        final String pre = RES + "chr/" + dir + "/" + name;
        final PlayerImagesSubSet basicSet = loadPlayerImagesSubSet(pre + ".png", name);
        return new PlayerImages(basicSet, null, null);
    }
    
    private final static PlayerImagesSubSet loadPlayerImagesSubSet(final String path, final String name) {
        final String pre = PRE_IMG + "." + name + ".";
        final Img[] imgs = Imtil.loadStrip(path, 32);
        final Panmage still = newPlayerImage(pre + "still", imgs[0]);
        final Panmage run1 = newPlayerImage(pre + "run.1", imgs[1]);
        final Panmage run2 = newPlayerImage(pre + "run.2", imgs[2]);
        final Panmage run3 = newPlayerImage(pre + "run.3", imgs[3]);
        final Panmage jump = newPlayerImage(pre + "jump", imgs[4]);
        return new PlayerImagesSubSet(still, jump, new Panmage[] { run1, run2, run3 });
    }
    
    private final static Panmage newPlayerImage(final String id, final Img img) {
        return Pangine.getEngine().createImage(id, og, ng, xg, img);
    }
    
    protected final static class BotsnBoltsScreen extends Panscreen {
        @Override
        protected final void load() throws Exception {
            final Panroom room = Pangame.getGame().getCurrentRoom();
            tm = new TileMap(Pantil.vmid(), room, 16, 16);
            for (int i = tm.getWidth() - 1; i >= 0; i--) {
                tm.setBehavior(i, 0, Tile.BEHAVIOR_SOLID);
            }
            final Player player = new Player(pc);
            player.getPosition().set(96, 96);
            room.addActor(player);
        }
    }
    
    public final static void main(final String[] args) {
        try {
            new BotsnBoltsGame().start();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }
}
