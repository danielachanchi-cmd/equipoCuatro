# Sonidos para pico botella

Coloca los archivos en esta carpeta del proyecto:

`app/src/main/res/raw/`

## Nombres obligatorios (minúsculas, sin espacios)

| Archivo | Uso |
|---------|-----|
| `game_music.mp3` o `game_music.wav` | Música de fondo del home (HU 2) |
| `bottle_spin.mp3` o `bottle_spin.wav` | Sonido mientras gira la botella (HU 11) |

Android genera el recurso `R.raw.game_music` a partir del nombre del archivo (sin extensión).

## Dónde descargar sonidos libres

### Botella girando
- [Freesound – buscar "bottle spin"](https://freesound.org/search/?q=bottle+spin)
- [OpenGameArt – efectos](https://opengameart.org/art-search-advanced?keys=bottle)
- [Pixabay – efectos](https://pixabay.com/sound-effects/search/bottle/)

### Música de fondo (loop corto, sin copyright)
- [Pixabay – música](https://pixabay.com/music/search/game/)
- [Free Music Archive](https://freemusicarchive.org/)
- [OpenGameArt – música](https://opengameart.org/art-search-advanced?keys=game+music)

## Licencias

Usa sonidos con licencia **CC0**, **dominio público** o que permitan uso educativo. En la sustentación menciona la fuente si el profesor lo pide.

## Cómo agregarlos en Android Studio

1. Clic derecho en `app/src/main/res/raw` → **New** → **Import resource**.
2. Elige el archivo descargado.
3. Renómbralo a `game_music` o `bottle_spin` si hace falta.
4. **Build → Rebuild Project** y prueba en el emulador.

Si no agregas archivos, la app sigue funcionando: el giro usa un tono de respaldo y la música de fondo queda desactivada hasta que exista `game_music`.
