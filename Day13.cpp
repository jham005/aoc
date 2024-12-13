#include <iostream>

class FindSum {
  const int aX, aY, bX, bY;
  const long prizeX, prizeY;

  bool plausible(long scale) {
    return scale * aX <= prizeX && scale * aY <= prizeY;
  }

public:
  FindSum(int _aX,
          int _aY,
          int _bX,
          int _bY,
          int _prizeX,
          int _prizeY,
          long _error)
    : aX(_aX), aY(_aY), bX(_bX), bY(_bY), prizeX(_prizeX + _error), prizeY(_prizeY + _error) {}

  long operator()() {
    std::cout
      << "Solving for A: X+" << aX << ", Y+" << aY
      << ", B: X+" << bX << ", Y=" << bY
      << ", Prize: X=" << prizeX << ", Y=" << prizeY << '\n';

    for (auto aStep = 0; ; aStep++) {
      if ((prizeX - aStep * aX) % bX != 0 || (prizeY - aStep * aY) % bY != 0)
        continue;
      if (!plausible(aStep))
        break;

      for (auto scale = 1; plausible(scale * aStep); scale++) {
        auto nA = scale * aStep;
        auto nB = (prizeX - nA * aX) / bX;
        if (nA * aX + nB * bX == prizeX && nA * aY + nB * bY == prizeY) {
          std::cout
            << "Solved: " << nA << 'x' << aX << " + " << nB << 'x' << bX
            << " = " << (nA * aX + nB * bX)
            << " = " << nA << 'x' << aY << " + " << nB << 'x' << bY
            << " = " << (nA * aY + nB * bY)
            << '\n';
          return 3 * nA + nB;
        }
      }
    }

    return 0;
  }
};

int main() {
  auto correction = 10000000000000L; // 10000000000000L
  auto sum =
    FindSum(94, 34, 22, 67, 8400, 5400, correction)() +
    FindSum(26, 66, 67, 21, 12748, 12176, correction)() +
    FindSum(17, 86, 84, 37, 7870, 6450, correction)() +
    FindSum(69, 23, 27, 71, 18641, 10279, correction)();
  std::cout << "Sum = " << sum << '\n';
  return 0;
}