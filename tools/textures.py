"""Draws every Baby Clock texture and the mod icon. Run from the repo root: python3 tools/textures.py

All drawn from scratch here (no Mojang art). Edit the pictures below and run it again.
"""
import math
import os
import struct
import zlib

HERE = os.path.dirname(__file__)
TEX = os.path.join(HERE, "..", "src", "main", "resources", "assets", "baby_clock", "textures")


def write_png(path, pixels, w, h):
    raw = b"".join(b"\x00" + bytes(c for px in pixels[y * w:(y + 1) * w] for c in px) for y in range(h))

    def chunk(kind, data):
        return struct.pack(">I", len(data)) + kind + data + struct.pack(">I", zlib.crc32(kind + data) & 0xFFFFFFFF)

    png = b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", struct.pack(">IIBBBBB", w, h, 8, 6, 0, 0, 0))
    png += chunk(b"IDAT", zlib.compress(raw, 9)) + chunk(b"IEND", b"")
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "wb") as f:
        f.write(png)


CLEAR = (0, 0, 0, 0)
PAL = {
    "P": (240, 150, 190, 255),   # clock pink
    "p": (214, 118, 162, 255),   # pink shadow
    "R": (186, 86, 132, 255),    # pink rim
    "W": (252, 250, 244, 255),   # clock face
    "k": (40, 34, 40, 255),      # hands / eyes
    "r": (220, 60, 70, 255),     # red middle
    "G": (240, 196, 70, 255),    # gold bells
    "g": (196, 150, 40, 255),    # gold shadow
    "S": (248, 200, 158, 255),   # baby skin
    "s": (224, 168, 128, 255),   # skin shadow / outline
    "H": (150, 98, 58, 255),     # hair
    "c": (244, 150, 150, 255),   # rosy cheeks
    "m": (70, 22, 26, 255),      # inside of the mouth
    "t": (228, 100, 120, 255),   # tongue
    "d": (168, 220, 255, 255),   # drool
    "D": (120, 190, 240, 255),   # drool shadow
    "B": (150, 200, 240, 255),   # icon background
    "b": (110, 160, 210, 255),   # icon background edge
}


def grid(rows, w=16, h=16, at=(0, 0)):
    px = [CLEAR] * (w * h)
    ox, oy = at
    for y, row in enumerate(rows):
        for x, ch in enumerate(row):
            if ch != ".":
                px[(oy + y) * w + ox + x] = PAL[ch]
    return px


def fill(ch_a, ch_b):
    """A whole 16x16 of one colour, with a little speckle of a second so it isn't flat."""
    return [PAL[ch_b] if (x * 7 + y * 13) % 11 == 0 else PAL[ch_a] for y in range(16) for x in range(16)]


# The clock face uses the block area x 4..12, y 4..12 (see models/block/baby_clock.json).
FACE = [
    "RRRRRRRR",
    "RWWWkWWR",
    "RWWWkWWR",
    "RkWWkWkR",
    "RWWWrkkR",
    "RWWWWWWR",
    "RWWWkWWR",
    "RRRRRRRR",
]

BABY = [
    "................",
    "......HHH.......",
    ".....H..HH......",
    "....ssssssss....",
    "...sSSSSSSSSs...",
    "..sSSSSSSSSSSs..",
    "..sSSkSSSSkSSs..",
    "..sSSkSSSSkSSs..",
    "..scSSSSSSSScs..",
    "..sccSSmmSSccs..",
    "..sSSSmttmSSSs..",
    "...sSSSmmSSSs...",
    "...sSSSSdSSSs...",
    "....sssSdsss....",
    "........D.......",
    "................",
]

MOUTH = [
    "mmmmmmmm",
    "mttmmttm",
    "........",
    "........",
    "dddd....",  # drool card: front at x 0, back at x 1
    "DDDD....",
]


def icon():
    n = 32
    px = [CLEAR] * (n * n)
    for y in range(n):
        for x in range(n):
            corner = min(x, n - 1 - x) + min(y, n - 1 - y)
            if corner < 2:
                continue
            edge = x in (0, n - 1) or y in (0, n - 1) or corner == 2
            px[y * n + x] = PAL["b"] if edge else PAL["B"]
    for y, row in enumerate(BABY):
        for x, ch in enumerate(row):
            if ch != ".":
                for sy in range(2):
                    for sx in range(2):
                        px[(y * 2 + sy) * n + x * 2 + sx] = PAL[ch]
    return [px[(y // 8) * n + x // 8] for y in range(n * 8) for x in range(n * 8)], n * 8


if __name__ == "__main__":
    write_png(os.path.join(TEX, "block", "baby_clock_face.png"), grid(FACE, at=(4, 4)), 16, 16)
    write_png(os.path.join(TEX, "block", "baby_clock_side.png"), fill("P", "p"), 16, 16)
    write_png(os.path.join(TEX, "block", "baby_clock_bell.png"), fill("G", "g"), 16, 16)
    write_png(os.path.join(TEX, "item", "baby_face.png"), grid(BABY), 16, 16)
    write_png(os.path.join(TEX, "entity", "baby_mouth.png"), grid(MOUTH), 16, 16)
    big, size = icon()
    write_png(os.path.join(HERE, "..", "src", "main", "resources", "assets", "icon.png"), big, size, size)
    print("done")
