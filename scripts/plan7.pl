#!/usr/bin/perl

my $monday = 0;
my $tuesday = 0;
my $wednesday = 0;
my $thursday = 0;
my $friday = 0;

# Pobieranie źródła strony w postaci zapisanego już pliku o nazwie "plik.htm"
open ( my $fileHandle, '<', 'plik.htm' );
 
# Sprawdzamy plik linia po linii
while ( my $line = <$fileHandle> ) {

  # $1 - nazwa, $2 - kod zajęć, $3 - grupa, $4 - dzień tygodnia, $5 - godzina, $6 - sala, $7 - prowadzący i reszta znaków
  if ($line =~ m|<td class="dol1">(.*)</td><td class="dol1">(.*)</td><td class="dol1">(.*)</td><td class="dol1">(.*)</td><td class="dol1">(.*)</td><td class="dol1">(.*)</td><td class="dol1">(.*)</td>|) {
    my $dzien = $4;
    my $godzina = $5;
    my $sala = $6;
    # Sprawdzamy czy godzina jest przed 12:00
    if ($godzina =~ m{0[0123456789]|1[01]}) {
      if ($sala =~ m{A1-}) {
        if ($dzien =~ m{Poniedziałek}) {
          $monday++;
        }
        if ($dzien =~ m{Wtorek}) {
          $tuesday++;
        }
        if ($dzien =~ m{Środa}) {
          $wednesday++;
        }
        if ($dzien =~ m{Czwartek}) {
          $thursday++;
        }
        if ($dzien =~ m{Piątek}) {
          $friday++;
        }
      }
    }
  }
 
}

print "Poniedziałek  x$monday\n";
print "Wtorek  x$tuesday\n";
print "Środa  x$wednesday\n";
print "Czwartek  x$thursday\n";
print "Piątek  x$friday\n";