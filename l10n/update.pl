#!/usr/bin/perl


#####################################################################
# This is a quick and dirty script which can update a ge.tt album if
# something has changed in the transifex repository. Pretty much
# everything is hardcoded for now.
# requires a work/ directory to exist, and a symlink called "last"
# to the folder containing the latest uploaded files.
# this is currently set to run via a cronjob, 3 minutes after every
# full hour. It only uploads a new file when something has changed.
#####################################################################


use strict;
use warnings;

my $DEBUG = 0;

system("wget -q -O .tx/config https://bitbucket.org/ixtab/jbpatcher/raw/master/l10n/.tx/config");
if (-s (".tx/config") <= 1) {
	die(".tx/config looks like it's in bad shape, aborting");
}

system("rm work/*.txt jbpatch-l10n-*.zip >/dev/null 2>&1");

my $LAST = `ls -l last | awk '{print \$NF;}'`;
chomp $LAST;

my $LAST_MD5 = `cd $LAST; md5sum *.txt|md5sum|awk '{print \$1}'`;
chomp $LAST_MD5;
print "last md5   : $LAST_MD5\n" if $DEBUG;

my $DATE = `date +%F-%R`;
chomp $DATE;

if (-d $DATE) {
	print "Warning: directory $DATE exists!\n";
	system("rm -f $DATE/*.txt");
} else {
	mkdir($DATE);
	print "directory $DATE created.\n" if $DEBUG;
}

system("ln -s ../.tx $DATE/.tx >/dev/null 2>&1") unless (-e "$DATE/.tx") ;
system("cd $DATE; tx pull -a -s --minimum-perc=100 >/dev/null 2>&1");

my $CURR_MD5 = `cd $DATE; md5sum *.txt|md5sum|awk '{print \$1}'`;
chomp $CURR_MD5;
print "current md5: $CURR_MD5\n" if $DEBUG;

if ($CURR_MD5 eq $LAST_MD5) {
	print "no changes, exiting.\n" if $DEBUG;
	system("rm -rf $DATE") unless $LAST eq $DATE;
	exit 0;
}

my @count = glob("$DATE/*.txt");
if (scalar(@count) == 0) {
	print "Current directory is empty, exiting!\n";
	system("rm -rf $DATE");
	exit 1;
}

system("cp $DATE/*.txt work/");
foreach my $f (glob("work/*.txt")) {
	&unescapeFile($f);
}

system("cd work; zip -9 ../jbpatch-l10n.zip *.txt >/dev/null 2>&1");
system("gett -s 2Cfq0KL jbpatch-l10n.zip >/dev/null 2>&1");

print "Updates uploaded.";# if $DEBUG;

system("rm work/*.txt jbpatch-l10n.zip >/dev/null 2>&1");

unless ($LAST eq $DATE) {
	system("rm last && ln -s $DATE last && rm -r $LAST");
}

exit 0;

sub unescapeFile {
	my ($f) = @_;
	open IN, $f or die $!;
	my @lines = <IN>;
	close IN;
	foreach my $i (0..$#lines) {
		$lines[$i] = &unescapeLine($lines[$i]);
	}
	open OUT, ">$f" or die $!;
	print OUT join("", @lines);
	close OUT;
}

sub unescapeLine {
	my ($l) = @_;
	my $n = $l;
	
	$n =~s |\\\\|\\|g;
	
	unless ($n eq $l) {
		if ($DEBUG) {
			print "BEFORE: $l";
			print "AFTER : $n";
		}
	}
	return $n;
}

